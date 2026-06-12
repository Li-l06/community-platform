package com.ll.project17.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ll.project17.common.Result;
import com.ll.project17.dto.HotQuestionVO;
import com.ll.project17.dto.QuestionDetailVO;
import com.ll.project17.dto.QuestionRequest;
import com.ll.project17.dto.QuestionVO;
import com.ll.project17.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/question")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping
    public Result<QuestionVO> publish(@Valid @RequestBody QuestionRequest request) {
        return Result.success(questionService.publish(request));
    }

    @GetMapping("/page")
    public Result<Page<QuestionVO>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(questionService.page(page, size));
    }
    @GetMapping("/{id}")
    public Result<QuestionDetailVO> detail(@PathVariable Long id) {
        return Result.success(questionService.detail(id));
    }
    @GetMapping("/hot")
    public Result<List<HotQuestionVO>> hot(@RequestParam(defaultValue = "10") int topN) {
        return Result.success(questionService.hotQuestions(topN));
    }
}