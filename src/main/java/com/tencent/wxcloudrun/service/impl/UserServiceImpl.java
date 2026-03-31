package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.dao.UserMapper;
import com.tencent.wxcloudrun.model.User;
import com.tencent.wxcloudrun.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 用户服务实现
 */
@Service
public class UserServiceImpl implements UserService {
    
    private final UserMapper userMapper;
    
    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
    
    @Override
    public User loginOrRegister(String openid, String nickname, String avatar) {
        Optional<User> existUser = userMapper.findByOpenid(openid);
        if (existUser.isPresent()) {
            // 用户已存在，更新信息
            User user = existUser.get();
            if (nickname != null || avatar != null) {
                user.setNickname(nickname);
                user.setAvatar(avatar);
                userMapper.update(user);
            }
            return user;
        }
        
        // 创建新用户
        User newUser = new User();
        newUser.setOpenid(openid);
        newUser.setNickname(nickname);
        newUser.setAvatar(avatar);
        userMapper.insert(newUser);
        return newUser;
    }
    
    @Override
    public Optional<User> findById(Long id) {
        return userMapper.findById(id);
    }
    
    @Override
    public Optional<User> findByOpenid(String openid) {
        return userMapper.findByOpenid(openid);
    }
    
    @Override
    public User updateUser(Long id, String nickname, String avatar) {
        Optional<User> userOpt = userMapper.findById(id);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("用户不存在");
        }
        User user = userOpt.get();
        user.setNickname(nickname);
        user.setAvatar(avatar);
        userMapper.update(user);
        return user;
    }
}
