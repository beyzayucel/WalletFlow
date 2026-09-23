package com.walletflow.auth.service;

import com.walletflow.auth.dto.request.LoginRequest;
import com.walletflow.auth.dto.request.RegisterRequest;
import com.walletflow.auth.dto.response.LoginResponse;

public interface AuthService {
    void userRegister(RegisterRequest request);
    LoginResponse login(LoginRequest loginRequest);
}
