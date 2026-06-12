package com.ll.project17.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ll.project17.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
