package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.HabitRequest;
import com.tencent.wxcloudrun.model.Habit;
import com.tencent.wxcloudrun.service.HabitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 习惯控制器
 */
@RestController
@RequestMapping("/api/habits")
public class HabitController {
    
    private final HabitService habitService;
    private final Logger logger = LoggerFactory.getLogger(HabitController.class);
    
    @Autowired
    public HabitController(HabitService habitService) {
        this.habitService = habitService;
    }
    
    /**
     * 获取用户所有习惯
     */
    @GetMapping
    public ApiResponse getHabits(@RequestParam Long userId) {
        logger.info("/api/habits get request, userId: {}", userId);
        List<Habit> habits = habitService.getHabitsByUserId(userId);
        return ApiResponse.ok(habits);
    }
    
    /**
     * 获取单个习惯
     */
    @GetMapping("/{id}")
    public ApiResponse getHabit(@PathVariable Long id) {
        logger.info("/api/habits/{} get request", id);
        return habitService.getHabitById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.error("习惯不存在"));
    }
    
    /**
     * 创建习惯
     */
    @PostMapping
    public ApiResponse createHabit(@RequestParam Long userId, @RequestBody HabitRequest request) {
        logger.info("/api/habits post request, userId: {}", userId);
        
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            return ApiResponse.error("习惯名称不能为空");
        }
        
        Habit habit = habitService.createHabit(userId, request);
        return ApiResponse.ok(habit);
    }
    
    /**
     * 更新习惯
     */
    @PutMapping("/{id}")
    public ApiResponse updateHabit(@PathVariable Long id, @RequestBody HabitRequest request) {
        logger.info("/api/habits/{} put request", id);
        
        try {
            Habit habit = habitService.updateHabit(id, request);
            return ApiResponse.ok(habit);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    /**
     * 删除习惯
     */
    @DeleteMapping("/{id}")
    public ApiResponse deleteHabit(@PathVariable Long id, @RequestParam Long userId) {
        logger.info("/api/habits/{} delete request, userId: {}", id, userId);
        
        try {
            habitService.deleteHabit(id, userId);
            return ApiResponse.ok("删除成功");
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
