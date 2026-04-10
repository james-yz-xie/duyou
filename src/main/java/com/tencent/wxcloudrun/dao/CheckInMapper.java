package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.CheckIn;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 打卡记录数据访问层
 */
@Mapper
public interface CheckInMapper {
    
    /**
     * 查询用户某习惯的所有打卡记录
     */
    List<CheckIn> findByHabitId(@Param("habitId") Long habitId);
    
    /**
     * 查询用户所有打卡记录
     */
    List<CheckIn> findByUserId(@Param("userId") Long userId);
    
    /**
     * 查询用户某天的所有打卡记录
     */
    List<CheckIn> findByUserIdAndDate(@Param("userId") Long userId, @Param("checkDate") LocalDate checkDate);
    
    /**
     * 查询某习惯某天是否已打卡
     */
    Optional<CheckIn> findByHabitIdAndDate(@Param("habitId") Long habitId, @Param("checkDate") LocalDate checkDate);
    
    /**
     * 创建打卡记录
     */
    int insert(CheckIn checkIn);
    
    /**
     * 删除打卡记录
     */
    int deleteByHabitIdAndDate(@Param("habitId") Long habitId, @Param("checkDate") LocalDate checkDate);
    
    /**
     * 删除某习惯的所有打卡记录
     */
    int deleteByHabitId(@Param("habitId") Long habitId);
    
    /**
     * 查询某习惯的打卡日期列表（用于计算连续天数）
     */
    List<LocalDate> findCheckDatesByHabitId(@Param("habitId") Long habitId);
    
    /**
     * 查询某习惯某月内的打卡记录
     */
    List<CheckIn> findByHabitIdAndMonth(@Param("habitId") Long habitId, @Param("year") int year, @Param("month") int month);
    
    /**
     * 统计某习惯某天的打卡次数
     */
    Integer countTodayCheckIn(@Param("habitId") Long habitId, @Param("checkDate") String checkDate);
}
