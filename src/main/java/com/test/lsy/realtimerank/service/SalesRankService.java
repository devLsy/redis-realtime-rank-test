package com.test.lsy.realtimerank.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SalesRankService {

    private final RedisTemplate redisTemplate;
    private final String salesLeaderboardKey = "sales_leaderboard";

    // 상품에 판매 수 추가
    public void addSales(String productId, double sales) {
        String productKey = "product:" + productId;
        redisTemplate.opsForZSet().add(salesLeaderboardKey, productKey, sales);
    }

    // 상품 판매 수 증가
    public void increaseSales(String productId, double sales) {
        String productKey = "product:" + productId;
        redisTemplate.opsForZSet().incrementScore(salesLeaderboardKey, productKey, sales);
    }

    // 상위 판매 상품 조회
    public List<String> getTopSellingProducts(int limit) {
        Set<ZSetOperations.TypedTuple<String>> orgResult =
                redisTemplate.opsForZSet().reverseRangeWithScores(salesLeaderboardKey, 0, limit - 1);

        return orgResult.stream()
                .map(tuple -> tuple.getValue().replace("Product ID:", "") + " (product: " + tuple.getScore() + ")")
                .collect(Collectors.toList());
    }
}
