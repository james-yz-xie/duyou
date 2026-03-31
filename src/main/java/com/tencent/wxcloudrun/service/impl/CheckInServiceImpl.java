package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.dao.CheckInMapper;
import com.tencent.wxcloudrun.dao.HabitMapper;
import com.tencent.wxcloudrun.dto.StatisticsResponse;
import com.tencent.wxcloudrun.model.CheckIn;
import com.tencent.wxcloudrun.model.Habit;
import com.tencent.wxcloudrun.service.CheckInService;
import com.tencent.wxcloudrun.service.HabitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

/**
 * 打卡服务实现
 */
@Service
public class CheckInServiceImpl implements CheckInService {
    
    private final CheckInMapper checkInMapper;
    private final HabitMapper habitMapper;
    private final HabitService habitService;
    
    @Autowired
    public CheckInServiceImpl(CheckInMapper checkInMapper, HabitMapper habitMapper, HabitService habitService) {
        this.checkInMapper = checkInMapper;
        this.habitMapper = habitMapper;
        this.habitService = habitService;
    }
    
    @Override
    @Transactional
    public CheckIn checkIn(Long userId, Long habitId, LocalDate date) {
        // 检查习惯是否存在且属于该用户
        Optional<Habit> habitOpt = habitMapper.findById(habitId);
        if (habitOpt.isEmpty()) {
            throw new RuntimeException("习惯不存在");
        }
        Habit habit = habitOpt.get();
        if (!habit.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此习惯");
        }
        
        // 检查是否已打卡
        if (isCheckedIn(habitId, date)) {
            throw new RuntimeException("今日已打卡");
        }
        
        // 创建打卡记录
        CheckIn checkIn = new CheckIn();
        checkIn.setHabitId(habitId);
        checkIn.setUserId(userId);
        checkIn.setCheckDate(date);
        checkInMapper.insert(checkIn);
        
        // 更新习惯统计
        int newStreak = calculateStreak(habitId);
        int newTotalCheckIns = habit.getTotalCheckIns() + 1;
        int newMaxStreak = Math.max(habit.getMaxStreak(), newStreak);
        habitService.updateHabitStats(habitId, newStreak, newMaxStreak, newTotalCheckIns);
        
        return checkIn;
    }
    
    @Override
    @Transactional
    public void uncheckIn(Long userId, Long habitId, LocalDate date) {
        // 检查习惯是否存在且属于该用户
        Optional<Habit> habitOpt = habitMapper.findById(habitId);
        if (habitOpt.isEmpty()) {
            throw new RuntimeException("习惯不存在");
        }
        Habit habit = habitOpt.get();
        if (!habit.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此习惯");
        }
        
        // 删除打卡记录
        checkInMapper.deleteByHabitIdAndDate(habitId, date);
        
        // 更新习惯统计
        int newStreak = calculateStreak(habitId);
        int newTotalCheckIns = Math.max(0, habit.getTotalCheckIns() - 1);
        habitService.updateHabitStats(habitId, newStreak, habit.getMaxStreak(), newTotalCheckIns);
    }
    
    @Override
    public boolean isCheckedIn(Long habitId, LocalDate date) {
        return checkInMapper.findByHabitIdAndDate(habitId, date).isPresent();
    }
    
    @Override
    public List<Map<String, Object>> getTodayStatus(Long userId) {
        List<Habit> habits = habitMapper.findByUserId(userId);
        LocalDate today = LocalDate.now();
        
        List<Map<String, Object>> result = new ArrayList<>();
        for (Habit habit : habits) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", habit.getId());
            item.put("name", habit.getName());
            item.put("icon", habit.getIcon());
            item.put("color", habit.getColor());
            item.put("streak", habit.getStreak());
            item.put("checkedInToday", isCheckedIn(habit.getId(), today));
            result.add(item);
        }
        return result;
    }
    
    @Override
    public StatisticsResponse getStatistics(Long userId) {
        List<Habit> habits = habitMapper.findByUserId(userId);
        LocalDate today = LocalDate.now();
        
        StatisticsResponse response = new StatisticsResponse();
        response.setTotalHabits(habits.size());
        
        int totalCheckIns = 0;
        int todayCheckedCount = 0;
        int maxStreak = 0;
        int totalStreak = 0;
        
        List<StatisticsResponse.HabitStatus> habitStatuses = new ArrayList<>();
        
        for (Habit habit : habits) {
            totalCheckIns += habit.getTotalCheckIns();
            maxStreak = Math.max(maxStreak, habit.getMaxStreak());
            totalStreak += habit.getStreak();
            
            boolean checkedInToday = isCheckedIn(habit.getId(), today);
            if (checkedInToday) {
                todayCheckedCount++;
            }
            
            StatisticsResponse.HabitStatus status = new StatisticsResponse.HabitStatus();
            status.setId(habit.getId());
            status.setName(habit.getName());
            status.setIcon(habit.getIcon());
            status.setColor(habit.getColor());
            status.setStreak(habit.getStreak());
            status.setCheckedInToday(checkedInToday);
            habitStatuses.add(status);
        }
        
        response.setTotalCheckIns(totalCheckIns);
        response.setTodayCheckedCount(todayCheckedCount);
        response.setTodayTotalCount(habits.size());
        response.setMaxStreak(maxStreak);
        response.setAvgStreak(habits.isEmpty() ? 0 : Math.round((float) totalStreak / habits.size()));
        response.setHabitStatuses(habitStatuses);
        
        return response;
    }
    
    @Override
    public List<CheckIn> getHabitCheckIns(Long habitId, int year, int month) {
        return checkInMapper.findByHabitIdAndMonth(habitId, year, month);
    }
    
    @Override
    public int calculateStreak(Long habitId) {
        List<LocalDate> dates = checkInMapper.findCheckDatesByHabitId(habitId);
        if (dates.isEmpty()) {
            return 0;
        }
        
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        
        // 按日期降序排序
        dates.sort(Collections.reverseOrder());
        
        // 检查今天或昨天是否打卡
        if (!dates.get(0).equals(today) && !dates.get(0).equals(yesterday)) {
            return 0;
        }
        
        int streak = 1;
        LocalDate expectedPrevDate = dates.get(0).minusDays(1);
        
        for (int i = 1; i < dates.size(); i++) {
            if (dates.get(i).equals(expectedPrevDate)) {
                streak++;
                expectedPrevDate = expectedPrevDate.minusDays(1);
            } else {
                break;
            }
        }
        
        return streak;
    }
}
