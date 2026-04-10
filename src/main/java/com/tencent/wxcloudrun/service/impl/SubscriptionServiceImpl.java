package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.dao.CheckInMapper;
import com.tencent.wxcloudrun.dao.HabitMapper;
import com.tencent.wxcloudrun.dao.SubscriptionMapper;
import com.tencent.wxcloudrun.dao.UserMapper;
import com.tencent.wxcloudrun.model.Habit;
import com.tencent.wxcloudrun.model.Subscription;
import com.tencent.wxcloudrun.model.User;
import com.tencent.wxcloudrun.service.SubscriptionService;
import com.tencent.wxcloudrun.service.WeChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 订阅消息服务实现
 */
@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    
    private static final Logger logger = LoggerFactory.getLogger(SubscriptionServiceImpl.class);
    
    // 订阅消息模板ID
    private static final String TEMPLATE_ID = "kBXtrNAwTn7m9oFnX464LTy_6KIvYdu6XaS42HXSONo";
    
    @Autowired
    private SubscriptionMapper subscriptionMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private HabitMapper habitMapper;
    
    @Autowired
    private CheckInMapper checkInMapper;
    
    @Autowired
    private WeChatService weChatService;
    
    @Override
    @Transactional
    public Subscription subscribe(Long userId, String openid, String templateId, boolean subscribe) {
        Optional<Subscription> existing = subscriptionMapper.findByUserIdAndTemplateId(userId, templateId);
        
        Subscription subscription;
        if (existing.isPresent()) {
            subscription = existing.get();
            subscription.setSubscribed(subscribe);
            if (subscribe) {
                subscriptionMapper.incrementRemainCount(subscription.getId(), 1);
            }
            subscription.setUpdatedAt(LocalDateTime.now());
            subscriptionMapper.update(subscription);
        } else {
            subscription = new Subscription();
            subscription.setUserId(userId);
            subscription.setOpenid(openid);
            subscription.setTemplateId(templateId);
            subscription.setSubscribed(subscribe);
            subscription.setRemainCount(subscribe ? 1 : 0);
            subscription.setCreatedAt(LocalDateTime.now());
            subscription.setUpdatedAt(LocalDateTime.now());
            subscriptionMapper.insert(subscription);
        }
        
        logger.info("用户 {} 订阅状态更新: {}", userId, subscribe ? "订阅" : "取消订阅");
        return subscription;
    }
    
    @Override
    public Subscription getSubscription(Long userId) {
        return subscriptionMapper.findByUserId(userId).orElse(null);
    }
    
    @Override
    public List<Subscription> getAllSubscribed() {
        return subscriptionMapper.findAllSubscribed();
    }
    
    private Map<String, Object> createValueMap(String value) {
        Map<String, Object> map = new HashMap<>();
        map.put("value", value);
        return map;
    }
    
    @Override
    public void sendCheckInReminder() {
        logger.info("开始发送打卡提醒...");
        
        List<Subscription> subscriptions = subscriptionMapper.findAllSubscribed();
        logger.info("找到 {} 个订阅用户", subscriptions.size());
        
        for (Subscription sub : subscriptions) {
            try {
                logger.info("处理用户 {} 的订阅, openid={}, remainCount={}", sub.getUserId(), sub.getOpenid(), sub.getRemainCount());
                
                Optional<User> userOpt = userMapper.findById(sub.getUserId());
                if (!userOpt.isPresent()) {
                    logger.warn("用户 {} 不存在", sub.getUserId());
                    continue;
                }
                
                List<Habit> habits = habitMapper.findByUserId(sub.getUserId());
                if (habits == null || habits.isEmpty()) {
                    logger.info("用户 {} 没有习惯，跳过", sub.getUserId());
                    continue;
                }
                
                String today = LocalDate.now().toString();
                List<Habit> uncheckedHabits = new ArrayList<>();
                for (Habit h : habits) {
                    Integer count = checkInMapper.countTodayCheckIn(h.getId(), today);
                    if (count == null || count == 0) {
                        uncheckedHabits.add(h);
                    }
                }
                
                int totalHabits = habits.size();
                int checkedCount = totalHabits - uncheckedHabits.size();
                
                String reminderText;
                if (uncheckedHabits.isEmpty()) {
                    reminderText = "太棒了！今日已全部打卡，继续保持！";
                } else {
                    reminderText = "还有" + uncheckedHabits.size() + "个习惯未打卡，快来完成吧~";
                }
                
                // 获取当前时间
                String currentTime = java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"));
                
                Map<String, Object> data = new HashMap<>();
                data.put("thing4", createValueMap(reminderText));
                data.put("time3", createValueMap(currentTime));
                
                logger.info("准备发送消息: openid={}, reminderText={}", sub.getOpenid(), reminderText);
                
                boolean success = weChatService.sendSubscribeMessage(
                    sub.getOpenid(),
                    TEMPLATE_ID,
                    data,
                    "pages/index/index"
                );
                
                if (success) {
                    subscriptionMapper.decrementRemainCount(sub.getId());
                    logger.info("用户 {} 打卡提醒发送成功", sub.getUserId());
                }
                
                Thread.sleep(200);
                
            } catch (Exception e) {
                logger.error("发送打卡提醒失败: userId={}", sub.getUserId(), e);
            }
        }
        
        logger.info("打卡提醒发送完成");
    }
}