package com.fzq.randapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/nonce")
@Tag(name = "Nonce Management", description = "APIs for managing nonce to ensure unique request handling")
public class NonceController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Operation(summary = "Check if nonce is already used", description = "This API checks if a nonce has already been used by querying Redis.")
    @GetMapping("/check")
    public ResponseEntity<Boolean> checkNonce(@RequestParam String nonce) {
        Boolean isNonceUsed = redisTemplate.hasKey("nonce:" + nonce);
        return ResponseEntity.ok(isNonceUsed != null && isNonceUsed);
    }
}
