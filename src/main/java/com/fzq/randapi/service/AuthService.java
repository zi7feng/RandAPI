package com.fzq.randapi.service;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    boolean verifyRequest(HttpServletRequest request);
}
