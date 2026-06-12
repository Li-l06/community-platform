package com.ll.project17.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionVO {
    private Long id;
    private String title;
    private String content;
    private Long userId;
    private String username;      // 发布者用户名
    private String tags;
    private Integer viewCount;
    private Integer answerCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}