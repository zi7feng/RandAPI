package com.fzq.randapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/nonce")
public class NonceController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkNonce(@RequestParam String nonce) {
        Boolean isNonceUsed = redisTemplate.hasKey("nonce:" + nonce);
        return ResponseEntity.ok(isNonceUsed != null && isNonceUsed);
    }
}
