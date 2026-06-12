package com.ll.project17.task;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ll.project17.entity.Answer;
import com.ll.project17.entity.Question;
import com.ll.project17.mapper.AnswerMapper;
import com.ll.project17.mapper.QuestionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class LikeSyncTask {

    private final RedisTemplate<String, Object> redisTemplate;
    private final QuestionMapper questionMapper;
    private final AnswerMapper answerMapper;

    //每 5 分钟同步一次 Redis 点赞数到 MySQL
    @Scheduled(fixedRate = 300000)
    public void syncLikeCount() {
        log.info("开始同步点赞数到数据库...");

        // 同步问题点赞数
        Set<String> questionKeys = redisTemplate.keys("like:question:*");
        if (questionKeys != null) {
            for (String key : questionKeys) {
                Long questionId = Long.parseLong(key.split(":")[2]);
                Long count = redisTemplate.opsForSet().size(key);
                LambdaUpdateWrapper<Question> wrapper = new LambdaUpdateWrapper<>();
                wrapper.eq(Question::getId, questionId)
                        .set(Question::getLikeCount, count.intValue());
                questionMapper.update(null, wrapper);
            }
        }

        // 同步回答点赞数
        Set<String> answerKeys = redisTemplate.keys("like:answer:*");
        if (answerKeys != null) {
            for (String key : answerKeys) {
                Long answerId = Long.parseLong(key.split(":")[2]);
                Long count = redisTemplate.opsForSet().size(key);
                LambdaUpdateWrapper<Answer> wrapper = new LambdaUpdateWrapper<>();
                wrapper.eq(Answer::getId, answerId)
                        .set(Answer::getLikeCount, count.intValue());
                answerMapper.update(null, wrapper);
            }
        }

        log.info("点赞数同步完成");
    }
}