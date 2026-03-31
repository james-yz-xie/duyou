package com.tencent.wxcloudrun.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 习惯实体
 */
public class Habit implements Serializable {
    
    private Long id;
    private Long userId;
    private String name;
    private String icon;
    private String color;
    private String reminder;
    private String frequency;
    private Integer streak;
    private Integer maxStreak;
    private Integer totalCheckIns;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public Habit() {}
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    
    public String getReminder() { return reminder; }
    public void setReminder(String reminder) { this.reminder = reminder; }
    
    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }
    
    public Integer getStreak() { return streak; }
    public void setStreak(Integer streak) { this.streak = streak; }
    
    public Integer getMaxStreak() { return maxStreak; }
    public void setMaxStreak(Integer maxStreak) { this.maxStreak = maxStreak; }
    
    public Integer getTotalCheckIns() { return totalCheckIns; }
    public void setTotalCheckIns(Integer totalCheckIns) { this.totalCheckIns = totalCheckIns; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
