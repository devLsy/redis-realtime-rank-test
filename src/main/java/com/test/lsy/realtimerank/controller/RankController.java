package com.test.lsy.realtimerank.controller;

import com.test.lsy.realtimerank.service.RankService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rank")
@RequiredArgsConstructor
public class RankController {

    private final RankService service;

    // 점수 추가 API
    @PostMapping("/add")
    public void addScore(@RequestParam String player, @RequestParam double score) {
        service.addScore(player, score);
    }

    // 점수 증가 API
    @PostMapping("/increase")
    public void increaseScore(@RequestParam String player, @RequestParam double score) {
        service.increaseScore(player, score);
    }

    // 상위 랭킹 조회 API
    @GetMapping("/top")
    public List<String> getTopPlayers(@RequestParam(defaultValue = "10") int limit) {
        return service.getTopPlayers(limit);
    }

    // 특정 플레이어 순위 조회 API
    @GetMapping("/rank")
    public Long getPlayerRank(@RequestParam String player) {
        return service.getPlayerRank(player);
    }
}
