package com.ll.project17.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
    public class User {

        private Long id;
        private String username;
        private String password; // 实际存加密后的
        private String avatar;
        private LocalDateTime createTime;
    }

