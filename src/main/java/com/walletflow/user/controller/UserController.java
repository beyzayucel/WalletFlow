package com.walletflow.user.controller;

import com.walletflow.auth.controller.BaseController;
import com.walletflow.common.response.ApiStandardResponse;
import com.walletflow.user.dto.RegisterRequest;
import com.walletflow.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController extends BaseController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiStandardResponse<Void>> userRegister(@Valid @RequestBody RegisterRequest request) {
        userService.userRegister(request);
        return created();
    }



}

