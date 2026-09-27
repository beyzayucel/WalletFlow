package com.walletflow.auth.service.impl;

import com.walletflow.auth.dto.request.LoginRequest;
import com.walletflow.auth.dto.request.RegisterRequest;
import com.walletflow.auth.dto.response.LoginResponse;
import com.walletflow.auth.exception.AuthException;
import com.walletflow.auth.exception.EmailAlreadyExistsException;
import com.walletflow.auth.exception.LoginLimitException;
import com.walletflow.auth.service.AuthService;
import com.walletflow.auth.ratelimiter.service.LoginRateLimitService;
import com.walletflow.auth.verificationtoken.service.VerificationTokenService;
import com.walletflow.monitoring.AppMetrics;
import com.walletflow.user.entity.Role;
import com.walletflow.user.entity.User;
import com.walletflow.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final VerificationTokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final AppMetrics appMetrics;
    private final LoginRateLimitService loginRateLimitService;


    @Override
    public void userRegister(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException();
        }

        User user = User.builder()
                .email(request.email())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .phoneNumber(request.phoneNumber())
                .role(Role.USER)
                .build();
        userRepository.save(user);
        tokenService.generateTokenAndSendEmail(user);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {

        User user = authenticateUser(loginRequest);
        if (!user.isEmailVerified()) {
            log.warn("Login rejected, email not verified: email={}", user.getEmail());
            throw new AuthException();
        }

        if (user.isFirstLogin()) {
            //jwt üret ve doğrudan giriş yap
        }


        return null;
    }

    private User authenticateUser(LoginRequest request) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        } catch (AuthenticationException exception) {
            appMetrics.incrementLoginFailure();
            boolean blocked = loginRateLimitService.incrementFailedAttempts(request.email());
            if(blocked){
                appMetrics.incrementAccountLocked();


                throw new LoginLimitException();
            }

        }

    }

}
