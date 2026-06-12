package com.ll.project17.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private String content;

    private Long userId;


    private String tags;

    private Integer viewCount;

    private Integer answerCount;

    private Integer likeCount;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}