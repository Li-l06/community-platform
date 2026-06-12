package com.ll.project17.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ll.project17.common.BusinessException;
import com.ll.project17.dto.LoginRequest;
import com.ll.project17.dto.RegisterRequest;
import com.ll.project17.dto.UserVO;
import com.ll.project17.entity.User;
import com.ll.project17.mapper.UserMapper;
import com.ll.project17.service.UserService;
import com.ll.project17.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public UserVO register(RegisterRequest request) {
        // 1. 检查用户名是否已存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, request.getUsername());
        if (userMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("用户名已存在");
        }

        // 2. 加密密码，保存用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userMapper.insert(user);

        // 3. 返回视图（注册不返回 token）
        return new UserVO(user.getId(), user.getUsername(), user.getAvatar(), null);
    }

    @Override
    public UserVO login(LoginRequest request) {
        // 1. 查用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, request.getUsername());
        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }

        // 2. 比对密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        // 3. 生成 JWT，返回带 token 的视图
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        return new UserVO(user.getId(), user.getUsername(), user.getAvatar(), token);
    }
}