CREATE DATABASE IF NOT EXISTS community_db
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE community_db;

CREATE TABLE IF NOT EXISTS `user` (
                                      `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '用户ID',
                                      `username`    VARCHAR(30)  NOT NULL COMMENT '用户名',
    `password`    VARCHAR(200) NOT NULL COMMENT '密码（BCrypt加密）',
    `avatar`      VARCHAR(500) DEFAULT NULL COMMENT '头像URL',
    `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

CREATE TABLE IF NOT EXISTS `question` (
                                          `id`           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '问题ID',
                                          `title`        VARCHAR(200) NOT NULL COMMENT '标题',
    `content`      TEXT         NOT NULL COMMENT '内容',
    `user_id`      BIGINT       NOT NULL COMMENT '发布者ID',
    `tags`         VARCHAR(500) DEFAULT NULL COMMENT '标签',
    `view_count`   INT          DEFAULT 0 COMMENT '浏览数',
    `answer_count` INT          DEFAULT 0 COMMENT '回答数',
    `like_count`   INT          DEFAULT 0 COMMENT '点赞数',
    `create_time`  DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_create_time` (`create_time`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='问题表';

CREATE TABLE IF NOT EXISTS `answer` (
                                        `id`          BIGINT    NOT NULL AUTO_INCREMENT COMMENT '回答ID',
                                        `content`     TEXT      NOT NULL COMMENT '内容',
                                        `question_id` BIGINT    NOT NULL COMMENT '问题ID',
                                        `user_id`     BIGINT    NOT NULL COMMENT '回答者ID',
                                        `parent_id`   BIGINT    DEFAULT 0 COMMENT '父回答ID',
                                        `like_count`  INT       DEFAULT 0 COMMENT '点赞数',
                                        `create_time` DATETIME  DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                        `update_time` DATETIME  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                        PRIMARY KEY (`id`),
    KEY `idx_question_id` (`question_id`),
    KEY `idx_user_id` (`user_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='回答表';

CREATE TABLE IF NOT EXISTS `notification` (
                                              `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '通知ID',
                                              `user_id`     BIGINT       NOT NULL COMMENT '接收用户ID',
                                              `content`     VARCHAR(500) NOT NULL COMMENT '通知内容',
    `type`        VARCHAR(20)  NOT NULL COMMENT '通知类型',
    `related_id`  BIGINT       DEFAULT NULL COMMENT '关联问题ID',
    `is_read`     TINYINT(1)   DEFAULT 0 COMMENT '是否已读',
    `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知表';