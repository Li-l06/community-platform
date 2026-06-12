package com.ll.project17.controller;

import com.ll.project17.common.Result;
import com.ll.project17.dto.NotificationVO;
import com.ll.project17.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public Result<List<NotificationVO>> list() {
        return Result.success(notificationService.list());
    }

    @GetMapping("/unread")
    public Result<Map<String, Long>> unreadCount() {
        Map<String, Long> map = new HashMap<>();
        map.put("count", notificationService.unreadCount());
        return Result.success(map);
    }

    @PutMapping("/read-all")
    public Result<Void> readAll() {
        notificationService.markAllRead();
        return Result.success();
    }
}