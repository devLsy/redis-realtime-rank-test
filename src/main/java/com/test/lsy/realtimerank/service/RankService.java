package com.test.lsy.realtimerank.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RankService {

    private final RedisTemplate redisTemplate;
    private final String leaderboardKey = "rank";

    // 점수 추가 (ZADD)
    public void addScore(String player, double score) {
        String playerKey = "rank:" + player;  // playerID에 "rank:" 접두어 추가
        redisTemplate.opsForZSet().add(leaderboardKey, playerKey, score);
    }

    // 점수 증가 (ZINCRBY)
    public void increaseScore(String player, double score) {
        String playerKey = "rank:" + player;  // playerID에 "rank:" 접두어 추가
        redisTemplate.opsForZSet().incrementScore(leaderboardKey, playerKey, score);
    }

    // 상위 랭킹 조회 (ZREVRANGE WITHSCORES)
    public Set<String> getTopPlayers(int limit) {
        Set<ZSetOperations.TypedTuple<String>> result =
                redisTemplate.opsForZSet().reverseRangeWithScores(leaderboardKey, 0, limit - 1);

        return result.stream()
                .map(tuple -> tuple.getValue().replace("rank:", "") + " (Score: " + tuple.getScore() + ")")
                .collect(Collectors.toSet());
    }

    // 특정 플레이어의 순위 조회 (ZRANK)
    public Long getPlayerRank(String player) {
        String playerKey = "rank:" + player;
        Long rank = redisTemplate.opsForZSet().reverseRank(leaderboardKey, playerKey);
        return (rank != null) ? rank + 1 : null; // 0부터 시작 → 1부터 시작하도록 변환 변환
    }
}
