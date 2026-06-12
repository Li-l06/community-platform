package com.ll.project17.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class QuestionRequest {

    @NotBlank(message = "Title cannot be empty")
    @Size(min = 5, max = 200, message = "Title 5-200 chars")
    private String title;

    @NotBlank(message = "Content cannot be empty")
    @Size(min = 10, message = "Content at least 10 chars")
    private String content;

//    标签，用逗号分隔
    private String tags;
}