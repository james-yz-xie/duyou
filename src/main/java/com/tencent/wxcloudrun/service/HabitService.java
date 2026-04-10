package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.dto.HabitRequest;
import com.tencent.wxcloudrun.model.Habit;

import java.util.List;
import java.util.Optional;

/**
 * 习惯服务接口
 */
public interface HabitService {
    
    /**
     * 获取用户所有习惯
     */
    List<Habit> getHabitsByUserId(Long userId);
    
    /**
     * 获取单个习惯
     */
    Optional<Habit> getHabitById(Long id);
    
    /**
     * 创建习惯
     */
    Habit createHabit(Long userId, HabitRequest request);
    
    /**
     * 更新习惯
     */
    Habit updateHabit(Long id, HabitRequest request);
    
    /**
     * 删除习惯
     */
    void deleteHabit(Long id, Long userId);
    
    /**
     * 清除用户所有习惯
     */
    void clearHabitsByUserId(Long userId);
    
    /**
     * 更新习惯统计数据
     */
    void updateHabitStats(Long habitId, int streak, int maxStreak, int totalCheckIns);
}
