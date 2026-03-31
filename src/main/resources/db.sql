-- 用户表
CREATE TABLE IF NOT EXISTS `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `openid` varchar(64) NOT NULL COMMENT '微信openid',
  `nickname` varchar(64) DEFAULT NULL,
  `avatar` varchar(256) DEFAULT NULL,
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_openid` (`openid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 习惯表
CREATE TABLE IF NOT EXISTS `habits` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `userId` bigint NOT NULL COMMENT '用户ID',
  `name` varchar(128) NOT NULL COMMENT '习惯名称',
  `icon` varchar(16) DEFAULT '📋',
  `color` varchar(16) DEFAULT '#4A90D9',
  `reminder` varchar(8) DEFAULT '' COMMENT '提醒时间 HH:mm',
  `frequency` varchar(16) DEFAULT 'daily',
  `streak` int DEFAULT 0 COMMENT '当前连续天数',
  `maxStreak` int DEFAULT 0 COMMENT '最大连续天数',
  `totalCheckIns` int DEFAULT 0 COMMENT '总打卡次数',
  `isActive` tinyint DEFAULT 1,
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='习惯表';

-- 打卡记录表
CREATE TABLE IF NOT EXISTS `check_ins` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `habitId` bigint NOT NULL,
  `userId` bigint NOT NULL,
  `checkDate` date NOT NULL,
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_habit_date` (`habitId`, `checkDate`),
  KEY `idx_userId` (`userId`),
  KEY `idx_date` (`checkDate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='打卡记录表';
