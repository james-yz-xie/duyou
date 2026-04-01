package com.tencent.wxcloudrun.dto;

import java.time.LocalDate;

/**
 * 打卡请求
 */
public class CheckInRequest {
    
    private Long habitId;
    private LocalDate checkDate;
    
    public CheckInRequest() {}
    
    public Long getHabitId() { return habitId; }
    public void setHabitId(Long habitId) { this.habitId = habitId; }
    
    public LocalDate getCheckDate() { return checkDate; }
    public void setCheckDate(LocalDate checkDate) { this.checkDate = checkDate; }
}
