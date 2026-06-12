package com.ll.project17.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerVO {
    private Long id;
    private String content;
    private Long questionId;
    private Long userId;
    private String username;
    private Long parentId;
    private Integer likeCount;
    private Boolean isLiked;  // 当前用户是否点过赞

}