package com.ll.project17.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AnswerRequest {

    @NotBlank
    @Size(min = 1, max = 5000)
    private String content;

    private Long parentId;  // Optional, 0 for top-level answer
}