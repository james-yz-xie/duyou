package com.tencent.wxcloudrun.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 订阅消息实体
 */
public class Subscription implements Serializable {
    
    private Long id;
    private Long userId;
    private String openid;
    private String templateId;
    private Boolean subscribed;
    private Integer remainCount;  // 剩余可发送次数
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public Subscription() {}
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public String getOpenid() { return openid; }
    public void setOpenid(String openid) { this.openid = openid; }
    
    public String getTemplateId() { return templateId; }
    public void setTemplateId(String templateId) { this.templateId = templateId; }
    
    public Boolean getSubscribed() { return subscribed; }
    public void setSubscribed(Boolean subscribed) { this.subscribed = subscribed; }
    
    public Integer getRemainCount() { return remainCount; }
    public void setRemainCount(Integer remainCount) { this.remainCount = remainCount; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}