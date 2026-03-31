package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.UserRequest;
import com.tencent.wxcloudrun.model.User;
import com.tencent.wxcloudrun.service.UserService;
import com.tencent.wxcloudrun.service.WeChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
    
    private final UserService userService;
    private final WeChatService weChatService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    public UserController(UserService userService, WeChatService weChatService) {
        this.userService = userService;
        this.weChatService = weChatService;
    }
    
    /**
     * 用户登录
     * 支持两种方式：
     * 1. 微信云托管环境：自动从请求头获取 openid
     * 2. 其他环境：通过 code 换取 openid
     */
    @PostMapping("/login")
    public ApiResponse login(@RequestBody UserRequest request,
                             @RequestHeader(value = "X-WX-OPENID", required = false) String headerOpenid) {
        logger.info("/api/user/login request");
        
        String openid = null;
        
        // 优先从请求头获取 openid（微信云托管环境）
        if (headerOpenid != null && !headerOpenid.isEmpty()) {
            openid = headerOpenid;
            logger.info("Got openid from header (WeChat Cloud Run)");
        }
        // 否则通过 code 换取 openid（其他环境）
        else if (request.getCode() != null && !request.getCode().isEmpty()) {
            openid = weChatService.getOpenidByCode(request.getCode());
            logger.info("Got openid from code exchange");
        }
        
        if (openid == null || openid.isEmpty()) {
            return ApiResponse.error("登录失败，请重试");
        }
        
        User user = userService.loginOrRegister(openid, request.getNickname(), request.getAvatar());
        
        Map<String, Object> result = new HashMap<>();
        result.put("id", user.getId());
        result.put("openid", user.getOpenid());
        result.put("nickname", user.getNickname());
        result.put("avatar", user.getAvatar());
        
        return ApiResponse.ok(result);
    }
    
    /**
     * 获取用户信息
     */
    @GetMapping("/info")
    public ApiResponse getUserInfo(@RequestParam Long userId) {
        logger.info("/api/user/info request, userId: {}", userId);
        
        return userService.findById(userId)
                .map(user -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("id", user.getId());
                    result.put("openid", user.getOpenid());
                    result.put("nickname", user.getNickname());
                    result.put("avatar", user.getAvatar());
                    return ApiResponse.ok(result);
                })
                .orElse(ApiResponse.error("用户不存在"));
    }
    
    /**
     * 更新用户信息
     */
    @PostMapping("/update")
    public ApiResponse updateUser(@RequestParam Long userId, @RequestBody UserRequest request) {
        logger.info("/api/user/update request, userId: {}", userId);
        
        User user = userService.updateUser(userId, request.getNickname(), request.getAvatar());
        
        Map<String, Object> result = new HashMap<>();
        result.put("id", user.getId());
        result.put("nickname", user.getNickname());
        result.put("avatar", user.getAvatar());
        
        return ApiResponse.ok(result);
    }
}
