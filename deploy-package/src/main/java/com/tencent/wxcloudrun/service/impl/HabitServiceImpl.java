package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.dao.CheckInMapper;
import com.tencent.wxcloudrun.dao.HabitMapper;
import com.tencent.wxcloudrun.dto.HabitRequest;
import com.tencent.wxcloudrun.model.Habit;
import com.tencent.wxcloudrun.service.HabitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 习惯服务实现
 */
@Service
public class HabitServiceImpl implements HabitService {
    
    private final HabitMapper habitMapper;
    private final CheckInMapper checkInMapper;
    
    @Autowired
    public HabitServiceImpl(HabitMapper habitMapper, CheckInMapper checkInMapper) {
        this.habitMapper = habitMapper;
        this.checkInMapper = checkInMapper;
    }
    
    @Override
    public List<Habit> getHabitsByUserId(Long userId) {
        return habitMapper.findByUserId(userId);
    }
    
    @Override
    public Optional<Habit> getHabitById(Long id) {
        return habitMapper.findById(id);
    }
    
    @Override
    public Habit createHabit(Long userId, HabitRequest request) {
        Habit habit = new Habit();
        habit.setUserId(userId);
        habit.setName(request.getName());
        habit.setIcon(request.getIcon() != null ? request.getIcon() : "📋");
        habit.setColor(request.getColor() != null ? request.getColor() : "#4A90D9");
        habit.setReminder(request.getReminder() != null ? request.getReminder() : "");
        habit.setFrequency(request.getFrequency() != null ? request.getFrequency() : "daily");
        habit.setStreak(0);
        habit.setMaxStreak(0);
        habit.setTotalCheckIns(0);
        habit.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        
        habitMapper.insert(habit);
        return habit;
    }
    
    @Override
    public Habit updateHabit(Long id, HabitRequest request) {
        Optional<Habit> habitOpt = habitMapper.findById(id);
        if (!habitOpt.isPresent()) {
            throw new RuntimeException("习惯不存在");
        }
        
        Habit habit = habitOpt.get();
        if (request.getName() != null) {
            habit.setName(request.getName());
        }
        if (request.getIcon() != null) {
            habit.setIcon(request.getIcon());
        }
        if (request.getColor() != null) {
            habit.setColor(request.getColor());
        }
        if (request.getReminder() != null) {
            habit.setReminder(request.getReminder());
        }
        if (request.getFrequency() != null) {
            habit.setFrequency(request.getFrequency());
        }
        if (request.getIsActive() != null) {
            habit.setIsActive(request.getIsActive());
        }
        
        habitMapper.update(habit);
        return habit;
    }
    
    @Override
    @Transactional
    public void deleteHabit(Long id, Long userId) {
        Optional<Habit> habitOpt = habitMapper.findById(id);
        if (!habitOpt.isPresent()) {
            throw new RuntimeException("习惯不存在");
        }
        
        Habit habit = habitOpt.get();
        if (!habit.getUserId().equals(userId)) {
            throw new RuntimeException("无权删除此习惯");
        }
        
        // 先删除打卡记录
        checkInMapper.deleteByHabitId(id);
        // 再删除习惯
        habitMapper.deleteById(id);
    }
    
    @Override
    public void updateHabitStats(Long habitId, int streak, int maxStreak, int totalCheckIns) {
        Optional<Habit> habitOpt = habitMapper.findById(habitId);
        if (habitOpt.isPresent()) {
            Habit habit = habitOpt.get();
            habit.setStreak(streak);
            habit.setMaxStreak(maxStreak);
            habit.setTotalCheckIns(totalCheckIns);
            habitMapper.update(habit);
        }
    }
}
