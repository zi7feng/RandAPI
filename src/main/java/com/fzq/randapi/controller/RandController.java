package com.fzq.randapi.controller;

import com.fzq.randapi.service.RandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Random Number API", description = "API for generating random numbers within a specific range.")
public class RandController {

    @Autowired
    private RandService randService;

    @Autowired

    private RedisTemplate<String, String> redisTemplate;

    @Operation(summary = "Generate a random number between 0 and 10",
            description = "This endpoint generates a random number between 0 and 10. The request must be signed with valid access credentials.")
    @GetMapping("/number/small")
    public int genRandNumberZeroToTen(HttpServletRequest request) {
        return randService.generateRandNumber(request);
    }


//    @GetMapping("/testConnection")
//    public String testConnection() {
//        try (Connection conn = DriverManager.getConnection(
//                "jdbc:mysql://34.217.100.182:3306/RANDAPI",
//                "root", "Gpt12345")) {
//            return "Connection successful!";
//        } catch (SQLException e) {
//            return "Connection failed: " + e.getMessage();
//        }
//    }

//    @GetMapping("/testRedisConnection")
//    public String testRedisConnection() {
//
//
//        try {
//            redisTemplate.opsForValue().set("testKey", "Hello Redis");
//
//            String value = redisTemplate.opsForValue().get("testKey");
//
//            return "Redis connection successful! Retrieved value: " + value;
//        } catch (Exception e) {
//            return "Redis connection failed: " + e.getMessage();
//        }
//    }

}
