package com.fzq.randapi.controller;

import com.fzq.randapi.service.RandService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rand")
public class RandController {

    @Autowired
    private RandService randService;

    @GetMapping("/number/small")
    public int genRandNumberZeroToTen(HttpServletRequest request) {
        return randService.generateRandNumber(request);
    }
}
