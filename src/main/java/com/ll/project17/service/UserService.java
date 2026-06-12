package com.ll.project17.service;

import com.ll.project17.dto.LoginRequest;
import com.ll.project17.dto.RegisterRequest;
import com.ll.project17.dto.UserVO;

public interface UserService {

   //  注册，返回用户视图
    UserVO register(RegisterRequest request);


    UserVO login(LoginRequest request);
}