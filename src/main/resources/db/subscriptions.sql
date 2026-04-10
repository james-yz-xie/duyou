-- 订阅消息表
CREATE TABLE IF NOT EXISTS subscriptions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    userId BIGINT NOT NULL COMMENT '用户ID',
    openid VARCHAR(64) NOT NULL COMMENT '用户openid',
    templateId VARCHAR(64) NOT NULL COMMENT '模板ID',
    subscribed TINYINT(1) DEFAULT 1 COMMENT '是否订阅',
    remainCount INT DEFAULT 0 COMMENT '剩余可发送次数',
    createdAt DATETIME DEFAULT CURRENT_TIMESTAMP,
    updatedAt DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_userId (userId),
    INDEX idx_openid (openid),
    UNIQUE KEY uk_user_template (userId, templateId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订阅消息表';