package com.walletflow.auth.service;

import com.walletflow.user.entity.User;

public interface TokenService<T> {
    String generateToken(User user);
    boolean validateToken(String token);
    User consumeToken(String token);
}
