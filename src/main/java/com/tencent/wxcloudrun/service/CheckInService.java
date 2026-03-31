package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.dto.StatisticsResponse;
import com.tencent.wxcloudrun.model.CheckIn;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 打卡服务接口
 */
public interface CheckInService {
    
    /**
     * 打卡
     */
    CheckIn checkIn(Long userId, Long habitId, LocalDate date);
    
    /**
     * 取消打卡
     */
    void uncheckIn(Long userId, Long habitId, LocalDate date);
    
    /**
     * 检查某天是否已打卡
     */
    boolean isCheckedIn(Long habitId, LocalDate date);
    
    /**
     * 获取用户今日打卡状态
     */
    List<Map<String, Object>> getTodayStatus(Long userId);
    
    /**
     * 获取统计数据
     */
    StatisticsResponse getStatistics(Long userId);
    
    /**
     * 获取习惯的打卡记录（某月）
     */
    List<CheckIn> getHabitCheckIns(Long habitId, int year, int month);
    
    /**
     * 计算连续打卡天数
     */
    int calculateStreak(Long habitId);
}
