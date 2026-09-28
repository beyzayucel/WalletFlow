package com.walletflow.auth.controller;

import com.walletflow.auth.controller.api.AuthApi;
import com.walletflow.auth.dto.request.RegisterRequest;
import com.walletflow.auth.service.AuthService;
import com.walletflow.auth.verificationtoken.dto.request.PasswordRequest;
import com.walletflow.auth.verificationtoken.service.VerificationTokenService;
import com.walletflow.common.controller.BaseController;
import com.walletflow.common.response.ApiStandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController extends BaseController implements AuthApi {

    private final VerificationTokenService tokenService;
    private final AuthService authService;

    @Override
    public ResponseEntity<ApiStandardResponse<Void>> userRegister(RegisterRequest request) {
        authService.userRegister(request);
        return created();
    }

    @Override
    public ResponseEntity<ApiStandardResponse<Boolean>> verifyToken(String token) {
        boolean isValid = tokenService.validateToken(token);
        return ok(isValid);
    }

    @Override
    public ResponseEntity<ApiStandardResponse<Void>> setPassword(PasswordRequest passwordRequest) {
        tokenService.completeRegistration(passwordRequest);
        return ok();
    }

//
//    public ResponseEntity<ApiStandardResponse<LoginResponse>> login(LoginRequest loginRequest) {
//        authService.login(loginRequest);
//        return ok();
    }
