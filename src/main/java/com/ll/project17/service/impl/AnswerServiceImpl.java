package com.ll.project17.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ll.project17.dto.AnswerRequest;
import com.ll.project17.dto.AnswerVO;
import com.ll.project17.entity.Answer;
import com.ll.project17.entity.Question;
import com.ll.project17.entity.User;
import com.ll.project17.mapper.AnswerMapper;
import com.ll.project17.mapper.QuestionMapper;
import com.ll.project17.mapper.UserMapper;
import com.ll.project17.service.AnswerService;
import com.ll.project17.service.NotificationService;
import com.ll.project17.util.SensitiveFilter;
import com.ll.project17.util.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

    private final AnswerMapper answerMapper;
    private final QuestionMapper questionMapper;
    private final UserMapper userMapper;
    private final NotificationService notificationService;
    private final SensitiveFilter sensitiveFilter;
    @Override
    @Transactional
    public AnswerVO answer(Long questionId, AnswerRequest request) {
        Long userId = UserContext.getUserId();

        // 1. 保存回答
        Answer answer = new Answer();
        BeanUtils.copyProperties(request, answer);
        answer.setContent(sensitiveFilter.filter(request.getContent()));
        answer.setQuestionId(questionId);
        answer.setUserId(userId);
        answer.setParentId(request.getParentId() != null ? request.getParentId() : 0L);
        answer.setLikeCount(0);
        answerMapper.insert(answer);

        // 2. 更新问题回答数
        LambdaUpdateWrapper<Question> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Question::getId, questionId)
                .setSql("answer_count = answer_count + 1");
        questionMapper.update(null, wrapper);

        // 3. 组装返回视图
        User user = userMapper.selectById(userId);
        AnswerVO vo = new AnswerVO();
        BeanUtils.copyProperties(answer, vo);
        vo.setUsername(user.getUsername());
        // 4. 异步通知提问者
        if (!userId.equals(questionMapper.selectById(questionId).getUserId())) {
            notificationService.send(
                    questionMapper.selectById(questionId).getUserId(),
                    "有人回答了你的问题",
                    "answer",
                    questionId
            );
        }
        return vo;

    }
}