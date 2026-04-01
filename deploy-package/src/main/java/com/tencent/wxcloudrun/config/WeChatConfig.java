package com.tencent.wxcloudrun.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 微信小程序配置
 */
@Configuration
@ConfigurationProperties(prefix = "wechat")
public class WeChatConfig {
    
    // 小程序 AppID
    private String appId;
    
    // 小程序 AppSecret
    private String appSecret;
    
    public String getAppId() { return appId; }
    public void setAppId(String appId) { this.appId = appId; }
    
    public String getAppSecret() { return appSecret; }
    public void setAppSecret(String appSecret) { this.appSecret = appSecret; }
}
