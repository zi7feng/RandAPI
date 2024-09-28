package com.fzq.randapi.controller;

import com.fzq.randapi.service.RandService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@RestController
@RequestMapping("/api/rand")
public class RandController {

    @Autowired
    private RandService randService;

    @Autowired

    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("/number/small")
    public int genRandNumberZeroToTen(HttpServletRequest request) {
        return randService.generateRandNumber(request);
    }


    @GetMapping("/testConnection")
    public String testConnection() {
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://my-rds.c7aw24i0uq3z.us-west-2.rds.amazonaws.com:3306/RANDAPI",
                "fzq", "Gpt12345")) {
            return "Connection successful!";
        } catch (SQLException e) {
            return "Connection failed: " + e.getMessage();
        }
    }

    @GetMapping("/testRedisConnection")
    public String testRedisConnection() {


        try {
            // 写入 Redis
            redisTemplate.opsForValue().set("testKey", "Hello Redis");

            // 从 Redis 读取
            String value = redisTemplate.opsForValue().get("testKey");

            return "Redis connection successful! Retrieved value: " + value;
        } catch (Exception e) {
            return "Redis connection failed: " + e.getMessage();
        }
    }

}
