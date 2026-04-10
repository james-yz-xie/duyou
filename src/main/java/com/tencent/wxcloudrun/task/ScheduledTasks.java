package com.tencent.wxcloudrun.task;

import com.tencent.wxcloudrun.service.SubscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务
 */
@Component
public class ScheduledTasks {
    
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
    
    @Autowired
    private SubscriptionService subscriptionService;
    
    /**
     * 每天12点发送打卡提醒
     */
    @Scheduled(cron = "0 0 12 * * ?")
    public void sendDailyReminder() {
        logger.info("执行每日打卡提醒任务");
        subscriptionService.sendCheckInReminder();
    }
}