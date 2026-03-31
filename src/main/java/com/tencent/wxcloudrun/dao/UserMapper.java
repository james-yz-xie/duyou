package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

/**
 * 用户数据访问层
 */
@Mapper
public interface UserMapper {
    
    /**
     * 根据 openid 查询用户
     */
    Optional<User> findByOpenid(@Param("openid") String openid);
    
    /**
     * 根据 id 查询用户
     */
    Optional<User> findById(@Param("id") Long id);
    
    /**
     * 创建用户
     */
    int insert(User user);
    
    /**
     * 更新用户信息
     */
    int update(User user);
}
