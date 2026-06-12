package com.ll.project17.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ll.project17.dto.NotificationVO;
import com.ll.project17.entity.Notification;
import com.ll.project17.mapper.NotificationMapper;
import com.ll.project17.service.NotificationService;
import com.ll.project17.util.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationMapper notificationMapper;

    @Override
    @Async
    public void send(Long userId, String content, String type, Long relatedId) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setContent(content);
        notification.setType(type);
        notification.setRelatedId(relatedId);
        notification.setIsRead(false);
        notificationMapper.insert(notification);
    }

    @Override
    public List<NotificationVO> list() {
        Long userId = UserContext.getUserId();
        LambdaQueryWrapper<Notification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notification::getUserId, userId)
                .orderByDesc(Notification::getCreateTime);
        List<Notification> notifications = notificationMapper.selectList(wrapper);

        return notifications.stream().map(n -> {
            NotificationVO vo = new NotificationVO();
            BeanUtils.copyProperties(n, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public Long unreadCount() {
        Long userId = UserContext.getUserId();
        LambdaQueryWrapper<Notification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notification::getUserId, userId)
                .eq(Notification::getIsRead, false);
        return notificationMapper.selectCount(wrapper);
    }

    @Override
    public void markAllRead() {
        Long userId = UserContext.getUserId();
        LambdaUpdateWrapper<Notification> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Notification::getUserId, userId)
                .set(Notification::getIsRead, true);
        notificationMapper.update(null, wrapper);
    }
}