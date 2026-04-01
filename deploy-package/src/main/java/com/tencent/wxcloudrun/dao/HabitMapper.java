package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.Habit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * 习惯数据访问层
 */
@Mapper
public interface HabitMapper {
    
    /**
     * 查询用户所有习惯
     */
    List<Habit> findByUserId(@Param("userId") Long userId);
    
    /**
     * 根据 id 查询习惯
     */
    Optional<Habit> findById(@Param("id") Long id);
    
    /**
     * 创建习惯
     */
    int insert(Habit habit);
    
    /**
     * 更新习惯
     */
    int update(Habit habit);
    
    /**
     * 删除习惯
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 根据用户 ID 删除所有习惯
     */
    int deleteByUserId(@Param("userId") Long userId);
}
