package com.ll.project17.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDetailVO {
    private Long id;
    private String title;
    private String content;
    private Long userId;
    private String username;
    private String tags;
    private Integer viewCount;
    private Integer answerCount;
    private LocalDateTime createTime;
    private List<AnswerVO> answers;
    private Boolean isLiked;  // 当前用户是否点过赞
}