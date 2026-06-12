package com.ll.project17.task;

import com.ll.project17.entity.Question;
import com.ll.project17.mapper.QuestionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class HotRankingTask {

    private final RedisTemplate<String, Object> redisTemplate;
    private final QuestionMapper questionMapper;

    private static final String HOT_KEY = "hot:questions";

   //每 30 秒重新计算热度排行
    @Scheduled(fixedRate = 30000)
    public void refreshHotRanking() {
        log.info("刷新热度排行榜...");

        // 查所有问题
        List<Question> questions = questionMapper.selectList(null);

        for (Question q : questions) {
            double score = calculateScore(q);
            redisTemplate.opsForZSet().add(HOT_KEY, q.getId().toString(), score);
        }

        log.info("热度排行榜刷新完成，共 {} 条", questions.size());
    }

    //热度分计算
    private double calculateScore(Question q) {
        int likeCount = q.getLikeCount() != null ? q.getLikeCount() : 0;
        int answerCount = q.getAnswerCount() != null ? q.getAnswerCount() : 0;

        // 计算问题存在的小时数
        long hours = Duration.between(q.getCreateTime(), LocalDateTime.now()).toHours();

        // 热度公式：点赞 × 2 + 回答 × 5 + 时间衰减
        return likeCount * 2.0 + answerCount * 5.0 + 100.0 / (hours + 2);
    }
}