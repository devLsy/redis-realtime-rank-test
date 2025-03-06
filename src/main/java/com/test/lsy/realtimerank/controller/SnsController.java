package com.test.lsy.realtimerank.controller;

import com.test.lsy.realtimerank.service.SnsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sns")
@RequiredArgsConstructor
public class SnsController {

    private final SnsService service;

    // 상품에 판매 수 추가 API
    @PostMapping("/add")
    public void addScore(@RequestParam String productId, @RequestParam double score) {
        service.addLikesToPost(productId, score);
    }

    // 상품 판매 수 증가 API
    @PostMapping("/increase")
    public void increaseScore(@RequestParam String productId, @RequestParam double score) {
        service.increaseLikesForPost(productId, score);
    }

    // 상위 판매 상품 조회 API
    @GetMapping("/top")
    public List<String> getTopPlayers(@RequestParam(defaultValue = "10") int limit) {
        return service.getTopPosts(limit);
    }
}
