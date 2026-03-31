package com.tencent.wxcloudrun.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tencent.wxcloudrun.config.WeChatConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * 微信登录服务
 * 通过 code 换取 openid
 */
@Service
public class WeChatService {
    
    private static final Logger logger = LoggerFactory.getLogger(WeChatService.class);
    private static final String JSCODE2SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session?appid={appid}&secret={secret}&js_code={code}&grant_type=authorization_code";
    
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final WeChatConfig weChatConfig;
    
    @Autowired
    public WeChatService(RestTemplate restTemplate, ObjectMapper objectMapper, WeChatConfig weChatConfig) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.weChatConfig = weChatConfig;
    }
    
    /**
     * 通过 code 换取 openid
     * @param code 小程序登录 code
     * @return openid，失败返回 null
     */
    public String getOpenidByCode(String code) {
        String appId = weChatConfig.getAppId();
        String appSecret = weChatConfig.getAppSecret();
        
        if (appId == null || appSecret == null || appId.isEmpty() || appSecret.isEmpty()) {
            logger.warn("AppId or AppSecret not configured");
            return null;
        }
        
        try {
            String url = JSCODE2SESSION_URL
                .replace("{appid}", appId)
                .replace("{secret}", appSecret)
                .replace("{code}", code);
            
            logger.info("Calling WeChat jscode2session API");
            String response = restTemplate.getForObject(url, String.class);
            
            if (response == null) {
                logger.error("WeChat API response is null");
                return null;
            }
            
            @SuppressWarnings("unchecked")
            Map<String, Object> result = objectMapper.readValue(response, Map.class);
            
            if (result.containsKey("openid")) {
                String openid = (String) result.get("openid");
                logger.info("Got openid successfully");
                return openid;
            } else {
                logger.error("WeChat API error: {}", result.get("errmsg"));
                return null;
            }
        } catch (Exception e) {
            logger.error("Failed to get openid from WeChat: {}", e.getMessage());
            return null;
        }
    }
}
