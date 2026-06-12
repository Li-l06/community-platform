# 社区问答平台（Community Q&A Platform）

仿知乎/牛客网精简版 Java 后端项目，Spring Boot 3 + MyBatis-Plus + Redis + JWT。

## 技术栈

- Java 17
- Spring Boot 3.5.x
- MyBatis-Plus 3.5.x
- MySQL 8.0
- Redis 7
- JWT 鉴权
- BCrypt 加密
- Docker Compose

## 功能模块

| 模块 | 说明 |
|------|------|
| 用户认证 | 注册/登录，JWT + BCrypt，ThreadLocal 存储当前用户 |
| 问题管理 | 发布问题、分页列表、详情（嵌套回答） |
| 回答功能 | 回答问题，回答数自动更新 |
| 点赞系统 | Redis Set 去重 + 计数，定时同步 MySQL |
| 热度排行 | Redis ZSet，点赞数+回答数+时间衰减算法 |
| 消息通知 | @Async 异步站内信，点赞/回答通知 |
| 敏感词过滤 | Trie 前缀树，O(n) 过滤 |
| 统一异常处理 | @RestControllerAdvice 全局拦截 |

## 本地运行

```bash
# 1. 确保 MySQL 和 Redis 已启动

# 2. 执行 init.sql 建表

# 3. 修改 application.yml 中 host 为 localhost

# 4. 启动
mvn spring-boot:run