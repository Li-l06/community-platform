package com.ll.project17.service;

import com.ll.project17.dto.NotificationVO;

import java.util.List;

public interface NotificationService {

   //发送通知异步
    void send(Long userId, String content, String type, Long relatedId);

   //获取当前用户的通知列表
    List<NotificationVO> list();

    //获取未读数量
    Long unreadCount();

     //全部标记为已读
    void markAllRead();
}