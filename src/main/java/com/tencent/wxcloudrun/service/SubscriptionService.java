package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.model.Subscription;
import java.util.List;

/**
 * 订阅消息服务接口
 */
public interface SubscriptionService {
    
    /**
     * 订阅消息
     */
    Subscription subscribe(Long userId, String openid, String templateId, boolean subscribe);
    
    /**
     * 获取用户订阅状态
     */
    Subscription getSubscription(Long userId);
    
    /**
     * 发送打卡提醒
     */
    void sendCheckInReminder();
    
    /**
     * 获取所有已订阅用户
     */
    List<Subscription> getAllSubscribed();
}