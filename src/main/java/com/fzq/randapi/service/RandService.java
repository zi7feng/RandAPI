package com.fzq.randapi.service;

import jakarta.servlet.http.HttpServletRequest;

public interface RandService {
    int generateRandNumber(HttpServletRequest request);
}
