package com.ll.project17.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ll.project17.dto.HotQuestionVO;
import com.ll.project17.dto.QuestionDetailVO;
import com.ll.project17.dto.QuestionRequest;
import com.ll.project17.dto.QuestionVO;

import java.util.List;

public interface QuestionService {


    QuestionVO publish(QuestionRequest request);
  //获取热度排行榜 Top
    List<HotQuestionVO> hotQuestions(int topN);
    QuestionDetailVO detail(Long id);

    Page<QuestionVO> page(int page, int size);
}
