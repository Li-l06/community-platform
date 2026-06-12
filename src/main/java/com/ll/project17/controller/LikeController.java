package com.ll.project17.controller;

import com.ll.project17.common.Result;
import com.ll.project17.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/like")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping
    public Result<Map<String, Object>> toggle(@RequestParam String type,
                                              @RequestParam Long targetId) {
        boolean liked = likeService.toggle(type, targetId);
        long count = likeService.count(type, targetId);

        Map<String, Object> map = new HashMap<>();
        map.put("liked", liked);
        map.put("count", count);
        return Result.success(map);
    }

    @GetMapping
    public Result<Map<String, Object>> status(@RequestParam String type,
                                              @RequestParam Long targetId) {
        long count = likeService.count(type, targetId);
        boolean liked = likeService.isLiked(type, targetId);

        Map<String, Object> map = new HashMap<>();
        map.put("liked", liked);
        map.put("count", count);
        return Result.success(map);
    }
}