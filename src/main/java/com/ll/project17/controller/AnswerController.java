package com.ll.project17.controller;

import com.ll.project17.common.Result;
import com.ll.project17.dto.AnswerRequest;
import com.ll.project17.dto.AnswerVO;
import com.ll.project17.service.AnswerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/question/{questionId}/answer")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    @PostMapping
    public Result<AnswerVO> answer(@PathVariable Long questionId,
                                   @Valid @RequestBody AnswerRequest request) {
        return Result.success(answerService.answer(questionId, request));
    }
}