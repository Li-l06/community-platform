package com.ll.project17.controller;

import com.ll.project17.common.Result;
import com.ll.project17.dto.LoginRequest;
import com.ll.project17.dto.RegisterRequest;
import com.ll.project17.dto.UserVO;
import com.ll.project17.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
   @Autowired
    private  UserService userService;

    @PostMapping("/register")
    public Result<UserVO> register(@Valid @RequestBody RegisterRequest request) {
        UserVO vo = userService.register(request);
        return Result.success(vo);
    }

    @PostMapping("/login")
    public Result<UserVO> login(@Valid @RequestBody LoginRequest request) {
        UserVO vo = userService.login(request);
        return Result.success(vo);
    }
}