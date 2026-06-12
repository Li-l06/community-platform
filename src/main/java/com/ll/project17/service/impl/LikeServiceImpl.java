package com.ll.project17.service.impl;

import com.ll.project17.entity.Answer;
import com.ll.project17.entity.Question;
import com.ll.project17.mapper.AnswerMapper;
import com.ll.project17.mapper.QuestionMapper;
import com.ll.project17.service.LikeService;
import com.ll.project17.service.NotificationService;
import com.ll.project17.util.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final RedisTemplate<String, Object> redisTemplate;

    private final NotificationService notificationService;
    private final QuestionMapper questionMapper;
    private final AnswerMapper answerMapper;
    @Override
    public boolean toggle(String targetType, Long targetId) {
        Long userId = UserContext.getUserId();
        String key = "like:" + targetType + ":" + targetId;

        Boolean isMember = redisTemplate.opsForSet().isMember(key, userId.toString());
        if (Boolean.TRUE.equals(isMember)) {
            // 已点过 → 取消点赞
            redisTemplate.opsForSet().remove(key, userId.toString());
            return false;  // false 表示取消点赞
        } else {
            // 没点过 → 点赞

            redisTemplate.opsForSet().add(key, userId.toString());
// 点赞成功时异步通知作者
            if ("question".equals(targetType)) {
                Question q = questionMapper.selectById(targetId);
                if (q != null && !userId.equals(q.getUserId())) {
                    notificationService.send(q.getUserId(), "有人点赞了你的问题", "like", targetId);
                }
            } else if ("answer".equals(targetType)) {
                Answer a = answerMapper.selectById(targetId);
                if (a != null && !userId.equals(a.getUserId())) {
                    notificationService.send(a.getUserId(), "有人点赞了你的回答", "like", a.getQuestionId());
                }
            }
            return true;   // true 表示点赞成功
        }
    }

    @Override
    public Long count(String targetType, Long targetId) {
        String key = "like:" + targetType + ":" + targetId;
        return redisTemplate.opsForSet().size(key);
    }

    @Override
    public boolean isLiked(String targetType, Long targetId) {
        Long userId = UserContext.getUserId();
        if (userId == null) return false;
        String key = "like:" + targetType + ":" + targetId;
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, userId.toString()));
    }
}