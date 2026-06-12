package com.ll.project17.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ll.project17.common.BusinessException;
import com.ll.project17.dto.*;
import com.ll.project17.entity.Answer;
import com.ll.project17.entity.Question;
import com.ll.project17.entity.User;
import com.ll.project17.mapper.AnswerMapper;
import com.ll.project17.mapper.QuestionMapper;
import com.ll.project17.mapper.UserMapper;
import com.ll.project17.service.LikeService;
import com.ll.project17.service.QuestionService;
import com.ll.project17.util.SensitiveFilter;
import com.ll.project17.util.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AnswerMapper answerMapper;
    @Autowired
    private LikeService likeService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Override
    public QuestionVO publish(QuestionRequest request) {
        Long userId = UserContext.getUserId();

        Question question = new Question();
        BeanUtils.copyProperties(request, question);
        question.setTitle(sensitiveFilter.filter(request.getTitle()));
        question.setContent(sensitiveFilter.filter(request.getContent()));
        question.setUserId(userId);
        question.setViewCount(0);
        question.setAnswerCount(0);
        question.setLikeCount(0);

        questionMapper.insert(question);

        User user = userMapper.selectById(userId);
        QuestionVO vo = new QuestionVO();
        BeanUtils.copyProperties(question, vo);
        vo.setUsername(user.getUsername());
        return vo;
    }

    @Override
    public Page<QuestionVO> page(int page, int size) {
        Page<Question> questionPage = new Page<>(page, size);
        LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Question::getCreateTime);

        questionPage = questionMapper.selectPage(questionPage, wrapper);

        List<Long> userIds = questionPage.getRecords().stream()
                .map(Question::getUserId)
                .distinct()
                .collect(Collectors.toList());

        Map<Long, String> userMap = userMapper.selectBatchIds(userIds).stream()
                .collect(Collectors.toMap(User::getId, User::getUsername));

        Page<QuestionVO> voPage = new Page<>(page, size, questionPage.getTotal());
        List<QuestionVO> voList = questionPage.getRecords().stream()
                .map(q -> {
                    QuestionVO vo = new QuestionVO();
                    BeanUtils.copyProperties(q, vo);
                    vo.setUsername(userMap.getOrDefault(q.getUserId(), "未知"));
                    return vo;
                })
                .collect(Collectors.toList());
        voPage.setRecords(voList);

        return voPage;
    }

    @Override
    public QuestionDetailVO detail(Long id) {
        // 1. 查问题
        Question question = questionMapper.selectById(id);
        if (question == null) {
            throw new BusinessException("问题不存在");
        }

        // 2. 浏览数 +1
        question.setViewCount(question.getViewCount() + 1);
        questionMapper.updateById(question);

        // 3. 查提问者
        User author = userMapper.selectById(question.getUserId());

        // 4. 查回答
        LambdaQueryWrapper<Answer> answerWrapper = new LambdaQueryWrapper<>();
        answerWrapper.eq(Answer::getQuestionId, id)
                .orderByAsc(Answer::getCreateTime);
        List<Answer> answers = answerMapper.selectList(answerWrapper);

        // 5. 批量查回答者用户名
        List<Long> answerUserIds = answers.stream()
                .map(Answer::getUserId)
                .distinct()
                .collect(Collectors.toList());
        Map<Long, String> userMap = userMapper.selectBatchIds(answerUserIds).stream()
                .collect(Collectors.toMap(User::getId, User::getUsername));

        // 6. 组装 AnswerVO
        List<AnswerVO> answerVOList = answers.stream()
                .map(a -> {
                    AnswerVO av = new AnswerVO();
                    BeanUtils.copyProperties(a, av);
                    av.setUsername(userMap.getOrDefault(a.getUserId(), "未知"));
                    try {
                        av.setIsLiked(likeService.isLiked("answer", a.getId()));
                    } catch (Exception e) {
                        av.setIsLiked(false);
                    }
                    return av;
                })
                .collect(Collectors.toList());

        // 7. 组装 QuestionDetailVO
        QuestionDetailVO vo = new QuestionDetailVO();
        BeanUtils.copyProperties(question, vo);
        vo.setUsername(author.getUsername());
        vo.setAnswers(answerVOList);
        try {
            vo.setIsLiked(likeService.isLiked("question", id));
        } catch (Exception e) {
            vo.setIsLiked(false);
        }

        return vo;
    }

    @Override
    public List<HotQuestionVO> hotQuestions(int topN) {
        String key = "hot:questions";
        // ZREVRANGE 取分数最高的前 N 个
        Set<Object> ids = redisTemplate.opsForZSet()
                .reverseRange(key, 0, topN - 1);

        if (ids == null || ids.isEmpty()) {
            // Redis 里还没有数据，从 MySQL 兜底
            LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
            wrapper.orderByDesc(Question::getCreateTime).last("LIMIT " + topN);
            List<Question> questions = questionMapper.selectList(wrapper);
            return questions.stream().map(q -> {
                HotQuestionVO vo = new HotQuestionVO();
                BeanUtils.copyProperties(q, vo);
                vo.setHotScore(0.0);
                return vo;
            }).collect(Collectors.toList());
        }

        List<Long> idList = ids.stream()
                .map(id -> Long.parseLong(id.toString()))
                .collect(Collectors.toList());

        // 批量查问题
        List<Question> questions = questionMapper.selectBatchIds(idList);
        Map<Long, Question> questionMap = questions.stream()
                .collect(Collectors.toMap(Question::getId, q -> q));

        // 批量查用户名
        List<Long> userIds = questions.stream()
                .map(Question::getUserId)
                .distinct()
                .collect(Collectors.toList());
        Map<Long, String> userMap = userMapper.selectBatchIds(userIds).stream()
                .collect(Collectors.toMap(User::getId, User::getUsername));

        // 按 ZSet 顺序组装（重要！ZREVRANGE 返回的就是排好序的）
        return idList.stream().map(id -> {
            Question q = questionMap.get(id);
            if (q == null) return null;
            HotQuestionVO vo = new HotQuestionVO();
            vo.setId(q.getId());
            vo.setTitle(q.getTitle());
            vo.setLikeCount(q.getLikeCount());
            vo.setAnswerCount(q.getAnswerCount());
            vo.setViewCount(q.getViewCount());
            vo.setUsername(userMap.getOrDefault(q.getUserId(), "未知"));
            Double score = redisTemplate.opsForZSet().score(key, id.toString());
            vo.setHotScore(score != null ? score : 0.0);
            return vo;
        }).filter(v -> v != null).collect(Collectors.toList());
    }
}