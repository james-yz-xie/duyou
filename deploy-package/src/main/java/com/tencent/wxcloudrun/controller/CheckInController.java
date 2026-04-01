package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.CheckInRequest;
import com.tencent.wxcloudrun.dto.StatisticsResponse;
import com.tencent.wxcloudrun.model.CheckIn;
import com.tencent.wxcloudrun.service.CheckInService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 打卡控制器
 */
@RestController
@RequestMapping("/api/checkin")
public class CheckInController {
    
    private final CheckInService checkInService;
    private final Logger logger = LoggerFactory.getLogger(CheckInController.class);
    
    @Autowired
    public CheckInController(CheckInService checkInService) {
        this.checkInService = checkInService;
    }
    
    /**
     * 打卡
     */
    @PostMapping
    public ApiResponse checkIn(@RequestParam Long userId, @RequestBody CheckInRequest request) {
        logger.info("/api/checkin post request, userId: {}, habitId: {}", userId, request.getHabitId());
        
        LocalDate date = request.getCheckDate() != null ? request.getCheckDate() : LocalDate.now();
        
        try {
            CheckIn checkIn = checkInService.checkIn(userId, request.getHabitId(), date);
            int streak = checkInService.calculateStreak(request.getHabitId());
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("streak", streak);
            result.put("checkIn", checkIn);
            
            return ApiResponse.ok(result);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    /**
     * 取消打卡
     */
    @DeleteMapping
    public ApiResponse uncheckIn(@RequestParam Long userId, @RequestParam Long habitId, 
                                  @RequestParam(required = false) String dateStr) {
        logger.info("/api/checkin delete request, userId: {}, habitId: {}", userId, habitId);
        
        LocalDate date = dateStr != null ? LocalDate.parse(dateStr) : LocalDate.now();
        
        try {
            checkInService.uncheckIn(userId, habitId, date);
            return ApiResponse.ok("取消成功");
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    /**
     * 获取今日状态
     */
    @GetMapping("/today")
    public ApiResponse getTodayStatus(@RequestParam Long userId) {
        logger.info("/api/checkin/today get request, userId: {}", userId);
        List<Map<String, Object>> status = checkInService.getTodayStatus(userId);
        return ApiResponse.ok(status);
    }
    
    /**
     * 获取统计数据
     */
    @GetMapping("/stats")
    public ApiResponse getStatistics(@RequestParam Long userId) {
        logger.info("/api/checkin/stats get request, userId: {}", userId);
        StatisticsResponse stats = checkInService.getStatistics(userId);
        return ApiResponse.ok(stats);
    }
    
    /**
     * 获取习惯的打卡记录（某月）
     */
    @GetMapping("/habit/{habitId}")
    public ApiResponse getHabitCheckIns(@PathVariable Long habitId, 
                                         @RequestParam int year, 
                                         @RequestParam int month) {
        logger.info("/api/checkin/habit/{} get request, year: {}, month: {}", habitId, year, month);
        List<CheckIn> checkIns = checkInService.getHabitCheckIns(habitId, year, month);
        return ApiResponse.ok(checkIns);
    }
}
