package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.model.User;

import java.util.Optional;

/**
 * 用户服务接口
 */
public interface UserService {
    
    /**
     * 根据 openid 获取或创建用户（登录）
     */
    User loginOrRegister(String openid, String nickname, String avatar);
    
    /**
     * 根据 id 获取用户
     */
    Optional<User> findById(Long id);
    
    /**
     * 根据 openid 获取用户
     */
    Optional<User> findByOpenid(String openid);
    
    /**
     * 更新用户信息
     */
    User updateUser(Long id, String nickname, String avatar);
    
    /**
     * 删除用户
     */
    void deleteUser(Long id);
}
