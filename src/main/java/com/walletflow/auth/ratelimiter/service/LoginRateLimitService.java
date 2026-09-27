package com.walletflow.auth.ratelimiter.service;

public interface LoginRateLimitService {
    boolean incrementFailedAttempts(String email);
}
