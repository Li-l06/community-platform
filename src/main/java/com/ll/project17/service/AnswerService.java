package com.ll.project17.service;

import com.ll.project17.dto.AnswerRequest;
import com.ll.project17.dto.AnswerVO;

public interface AnswerService {


    AnswerVO answer(Long questionId, AnswerRequest request);
}