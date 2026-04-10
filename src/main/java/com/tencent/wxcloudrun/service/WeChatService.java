package com.tencent.wxcloudrun.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tencent.wxcloudrun.config.WeChatConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 微信API服务
 */
@Service
public class WeChatService {
    
    private static final Logger logger = LoggerFactory.getLogger(WeChatService.class);
    
    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={appid}&secret={secret}";
    private static final String SEND_SUBSCRIBE_URL = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token={token}";
    private static final String CODE2SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session?appid={appid}&secret={secret}&js_code={code}&grant_type=authorization_code";
    
    @Autowired
    private WeChatConfig weChatConfig;
    
    @Autowired
    private RestTemplate restTemplate;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    // 缓存access_token
    private final Map<String, TokenCache> tokenCache = new ConcurrentHashMap<>();
    
    /**
     * 获取access_token
     */
    public String getAccessToken() {
        String appId = weChatConfig.getAppId();
        String appSecret = weChatConfig.getAppSecret();
        
        // 检查缓存
        TokenCache cache = tokenCache.get(appId);
        if (cache != null && cache.expiresAt > System.currentTimeMillis()) {
            return cache.token;
        }
        
        // 请求新token
        try {
            String url = ACCESS_TOKEN_URL.replace("{appid}", appId).replace("{secret}", appSecret);
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            
            Map<String, Object> result = objectMapper.readValue(response.getBody(), Map.class);
            
            if (result.containsKey("access_token")) {
                String token = (String) result.get("access_token");
                int expiresIn = (Integer) result.get("expires_in");
                
                // 缓存token，提前5分钟过期
                tokenCache.put(appId, new TokenCache(token, System.currentTimeMillis() + (expiresIn - 300) * 1000L));
                
                logger.info("获取access_token成功");
                return token;
            } else {
                logger.error("获取access_token失败: {}", result);
                return null;
            }
        } catch (Exception e) {
            logger.error("获取access_token异常", e);
            return null;
        }
    }
    
    /**
     * 发送订阅消息
     * @param openid 用户openid
     * @param templateId 模板ID
     * @param data 模板数据
     * @param page 跳转页面
     * @return 是否发送成功
     */
    public boolean sendSubscribeMessage(String openid, String templateId, Map<String, Object> data, String page) {
        String accessToken = getAccessToken();
        if (accessToken == null) {
            logger.error("获取access_token失败，无法发送订阅消息");
            return false;
        }
        
        try {
            String url = SEND_SUBSCRIBE_URL.replace("{token}", accessToken);
            
            Map<String, Object> body = new HashMap<>();
            body.put("touser", openid);
            body.put("template_id", templateId);
            body.put("page", page != null ? page : "pages/index/index");
            body.put("data", data);
            body.put("miniprogram_state", "formal"); // formal-正式版, developer-开发版, trial-体验版
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(body), headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            
            Map<String, Object> result = objectMapper.readValue(response.getBody(), Map.class);
            int errcode = result.containsKey("errcode") ? ((Number) result.get("errcode")).intValue() : -1;
            
            if (errcode == 0) {
                logger.info("订阅消息发送成功: openid={}", openid);
                return true;
            } else {
                logger.error("订阅消息发送失败: {}, openid={}", result, openid);
                return false;
            }
        } catch (Exception e) {
            logger.error("发送订阅消息异常", e);
            return false;
        }
    }
    
    /**
     * 通过 code 获取 openid
     */
    public String getOpenidByCode(String code) {
        String appId = weChatConfig.getAppId();
        String appSecret = weChatConfig.getAppSecret();
        
        try {
            String url = CODE2SESSION_URL
                .replace("{appid}", appId)
                .replace("{secret}", appSecret)
                .replace("{code}", code);
            
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            
            Map<String, Object> result = objectMapper.readValue(response.getBody(), Map.class);
            
            if (result.containsKey("openid")) {
                String openid = (String) result.get("openid");
                logger.info("通过code获取openid成功: {}", openid);
                return openid;
            } else {
                logger.error("获取openid失败: {}", result);
                return null;
            }
        } catch (Exception e) {
            logger.error("获取openid异常", e);
            return null;
        }
    }
    
    /**
     * Token缓存
     */
    private static class TokenCache {
        String token;
        long expiresAt;
        
        TokenCache(String token, long expiresAt) {
            this.token = token;
            this.expiresAt = expiresAt;
        }
    }
}