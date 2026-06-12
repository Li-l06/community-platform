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
public class Notification {

    @TableId(type = IdType.AUTO)
    private Long id;


    private Long userId;


    private String content;


    private String type;


    private Long relatedId;


    private Boolean isRead;


    private LocalDateTime createTime;
}