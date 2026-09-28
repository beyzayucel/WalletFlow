package com.walletflow.user.service;

import com.walletflow.user.entity.User;

public interface UserService {
    User findByEmail(String email);
}
