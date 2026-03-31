package com.tencent.wxcloudrun.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 统计数据响应
 */
public class StatisticsResponse {
    
    private Integer totalHabits;
    private Integer totalCheckIns;
    private Integer todayCheckedCount;
    private Integer todayTotalCount;
    private Integer avgStreak;
    private Integer maxStreak;
    private List<HabitStatus> habitStatuses = new ArrayList<>();
    
    public StatisticsResponse() {}
    
    public Integer getTotalHabits() { return totalHabits; }
    public void setTotalHabits(Integer totalHabits) { this.totalHabits = totalHabits; }
    
    public Integer getTotalCheckIns() { return totalCheckIns; }
    public void setTotalCheckIns(Integer totalCheckIns) { this.totalCheckIns = totalCheckIns; }
    
    public Integer getTodayCheckedCount() { return todayCheckedCount; }
    public void setTodayCheckedCount(Integer todayCheckedCount) { this.todayCheckedCount = todayCheckedCount; }
    
    public Integer getTodayTotalCount() { return todayTotalCount; }
    public void setTodayTotalCount(Integer todayTotalCount) { this.todayTotalCount = todayTotalCount; }
    
    public Integer getAvgStreak() { return avgStreak; }
    public void setAvgStreak(Integer avgStreak) { this.avgStreak = avgStreak; }
    
    public Integer getMaxStreak() { return maxStreak; }
    public void setMaxStreak(Integer maxStreak) { this.maxStreak = maxStreak; }
    
    public List<HabitStatus> getHabitStatuses() { return habitStatuses; }
    public void setHabitStatuses(List<HabitStatus> habitStatuses) { this.habitStatuses = habitStatuses; }
    
    /**
     * 习惯状态
     */
    public static class HabitStatus {
        private Long id;
        private String name;
        private String icon;
        private String color;
        private Integer streak;
        private Boolean checkedInToday;
        
        public HabitStatus() {}
        
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getIcon() { return icon; }
        public void setIcon(String icon) { this.icon = icon; }
        
        public String getColor() { return color; }
        public void setColor(String color) { this.color = color; }
        
        public Integer getStreak() { return streak; }
        public void setStreak(Integer streak) { this.streak = streak; }
        
        public Boolean getCheckedInToday() { return checkedInToday; }
        public void setCheckedInToday(Boolean checkedInToday) { this.checkedInToday = checkedInToday; }
    }
}
