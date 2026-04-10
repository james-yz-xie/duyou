package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.Subscription;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * 订阅消息数据访问层
 */
@Mapper
public interface SubscriptionMapper {
    
    /**
     * 根据用户ID和模板ID查询订阅
     */
    Optional<Subscription> findByUserIdAndTemplateId(@Param("userId") Long userId, @Param("templateId") String templateId);
    
    /**
     * 根据用户ID查询订阅
     */
    Optional<Subscription> findByUserId(@Param("userId") Long userId);
    
    /**
     * 插入订阅记录
     */
    int insert(Subscription subscription);
    
    /**
     * 更新订阅状态
     */
    int update(Subscription subscription);
    
    /**
     * 增加订阅次数
     */
    int incrementRemainCount(@Param("id") Long id, @Param("count") Integer count);
    
    /**
     * 减少订阅次数
     */
    int decrementRemainCount(@Param("id") Long id);
    
    /**
     * 查询所有已订阅且有余量的用户
     */
    List<Subscription> findAllSubscribed();
    
    /**
     * 删除用户订阅
     */
    int deleteByUserId(@Param("userId") Long userId);
}