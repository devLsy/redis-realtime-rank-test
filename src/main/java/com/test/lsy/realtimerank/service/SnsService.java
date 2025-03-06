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
public class SnsService {

    private final RedisTemplate redisTemplate;
    private final String snsPostKey  = "post_leaderboard";

    // 게시물에 좋아요 추가
    public void addLikesToPost(String postId, double likes) {
        String postKey = "post:" + postId;
        redisTemplate.opsForZSet().add(snsPostKey, postKey, likes);
    }

    // 게시물에 좋아요 증가
    public void increaseLikesForPost(String postId, double likes) {
        String postKey = "post:" + postId;
        redisTemplate.opsForZSet().incrementScore(snsPostKey, postKey, likes);
    }

    // 인기 게시글 조회
    public List<String> getTopPosts(int limit) {
        Set<ZSetOperations.TypedTuple<String>> orgResult = redisTemplate.opsForZSet().reverseRangeWithScores(snsPostKey, 0, limit - 1);

        return orgResult.stream()
                .map(tuple -> tuple.getValue().replace("Post ID:", "") + " (post: " + tuple.getScore() + ")")
                .collect(Collectors.toList());

    }

}
