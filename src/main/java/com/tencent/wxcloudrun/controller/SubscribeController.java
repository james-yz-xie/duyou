package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dao.UserMapper;
import com.tencent.wxcloudrun.model.Subscription;
import com.tencent.wxcloudrun.model.User;
import com.tencent.wxcloudrun.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 订阅消息控制器
 */
@RestController
@RequestMapping("/api/subscribe")
public class SubscribeController {
    
    @Autowired
    private SubscriptionService subscriptionService;
    
    @Autowired
    private UserMapper userMapper;
    
    /**
     * 订阅/取消订阅
     */
    @PostMapping
    public ApiResponse subscribe(
            @RequestParam Long userId,
            @RequestBody Map<String, Object> body) {
        
        boolean subscribe = Boolean.TRUE.equals(body.get("subscribe"));
        String templateId = (String) body.getOrDefault("templateId", "kBXtrNAwTn7m9oFnX464LTy_6KIvYdu6XaS42HXSONo");
        
        // 获取用户openid
        Optional<User> userOpt = userMapper.findById(userId);
        if (!userOpt.isPresent()) {
            return ApiResponse.error("用户不存在");
        }
        
        User user = userOpt.get();
        Subscription subscription = subscriptionService.subscribe(userId, user.getOpenid(), templateId, subscribe);
        
        Map<String, Object> result = new HashMap<>();
        result.put("id", subscription.getId());
        result.put("userId", subscription.getUserId());
        result.put("subscribed", subscription.getSubscribed());
        result.put("remainCount", subscription.getRemainCount());
        
        return ApiResponse.ok(result);
    }
    
    /**
     * 获取订阅状态
     */
    @GetMapping("/status")
    public ApiResponse getStatus(@RequestParam Long userId) {
        Subscription subscription = subscriptionService.getSubscription(userId);
        
        Map<String, Object> result = new HashMap<>();
        if (subscription != null) {
            result.put("subscribed", subscription.getSubscribed());
            result.put("remainCount", subscription.getRemainCount());
        } else {
            result.put("subscribed", false);
            result.put("remainCount", 0);
        }
        
        return ApiResponse.ok(result);
    }
    
    /**
     * 手动触发提醒（测试用）
     */
    @PostMapping("/trigger")
    public ApiResponse triggerReminder() {
        subscriptionService.sendCheckInReminder();
        return ApiResponse.ok("提醒已发送");
    }
}