package com.ll.project17.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationVO {
    private Long id;
    private String content;
    private String type;
    private Long relatedId;
    private Boolean isRead;
    private LocalDateTime createTime;
}