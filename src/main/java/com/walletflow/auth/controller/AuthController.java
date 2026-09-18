package com.walletflow.auth.controller;

import com.walletflow.auth.dto.request.RegisterRequest;
import com.walletflow.auth.service.AuthService;
import com.walletflow.auth.verificationtoken.dto.request.PasswordRequest;
import com.walletflow.auth.verificationtoken.service.VerificationTokenService;
import com.walletflow.common.controller.BaseController;
import com.walletflow.common.response.ApiStandardResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController extends BaseController {

    private final VerificationTokenService tokenService;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiStandardResponse<Void>> userRegister(@Valid @RequestBody RegisterRequest request) {
        authService.userRegister(request);
        return created();
    }

    @GetMapping("/verify")
    public ResponseEntity<ApiStandardResponse<Boolean>> verifyToken(@RequestParam String token) {
        boolean isValid = tokenService.isVerifyToken(token);
        return ok(isValid);
    }

    @PostMapping("/set-password")
    public ResponseEntity<ApiStandardResponse<Void>> setPassword(@Valid @RequestBody PasswordRequest passwordRequest) {
        tokenService.completeRegistration(passwordRequest);
        return ok();
    }

}
