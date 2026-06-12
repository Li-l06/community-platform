package com.ll.project17.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotQuestionVO {
    private Long id;
    private String title;
    private String username;
    private Integer likeCount;
    private Integer answerCount;
    private Integer viewCount;
   // 热度分
    private Double hotScore;
}