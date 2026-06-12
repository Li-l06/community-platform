package com.ll.project17.service;

public interface LikeService {

   //点赞/取消点赞，targetType 为 question 或 answer
    boolean toggle(String targetType, Long targetId);

  //  获取点赞数
    Long count(String targetType, Long targetId);

    //当前用户是否点过赞
    boolean isLiked(String targetType, Long targetId);
}