package com.walletflow.auth.service.impl;

import com.walletflow.auth.dto.request.LoginRequest;
import com.walletflow.auth.dto.request.RegisterRequest;
import com.walletflow.auth.dto.response.LoginResponse;
import com.walletflow.auth.exception.AuthErrorType;
import com.walletflow.auth.exception.AuthException;
import com.walletflow.auth.exception.LoginLimitException;
import com.walletflow.auth.exception.UserException;
import com.walletflow.auth.ratelimiter.config.LoginRateLimitProperties;
import com.walletflow.auth.refreshtoken.dto.RefreshTokenRequest;
import com.walletflow.auth.refreshtoken.service.RefreshTokenService;
import com.walletflow.auth.service.AuthService;
import com.walletflow.auth.ratelimiter.service.LoginRateLimitService;
import com.walletflow.auth.service.TokenService;
import com.walletflow.monitoring.AppMetrics;
import com.walletflow.security.config.JwtProperties;
import com.walletflow.security.service.JwtService;
import com.walletflow.user.entity.Role;
import com.walletflow.user.entity.User;
import com.walletflow.user.exception.UserErrorType;
import com.walletflow.user.repository.UserRepository;
import com.walletflow.user.service.UserService;
import com.walletflow.user.utils.EmailNormalizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final AppMetrics appMetrics;
    private final LoginRateLimitService loginRateLimitService;
    private final UserService userService;
    private final LoginRateLimitProperties loginRateLimitProperties;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final JwtProperties jwtProperties;


    @Override
    @Transactional
    public void userRegister(RegisterRequest request) {
        checkDuplicateUser(EmailNormalizer.normalize(request.email()), request.phoneNumber());

        User user = User.builder()
                .email(EmailNormalizer.normalize(request.email()))
                .firstName(request.firstName())
                .lastName(request.lastName())
                .phoneNumber(request.phoneNumber())
                .role(Role.USER)
                .build();

        userRepository.save(user);
        tokenService.generateToken(user);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        User user = authenticateUser(loginRequest);
        loginRateLimitService.resetAttemptsByHash(loginRequest.email());

        if (!user.isEmailVerified()) {
            log.warn("Login rejected, email not verified: email={}", user.getEmail());
            throw new AuthException(AuthErrorType.EMAIL_NOT_VERIFIED);
        }
        userService.updateLastLogin(user);

        if (user.isFirstLogin()) {
            appMetrics.incrementLoginSuccess();
            log.info("First login, OTP bypassed: event=USER_FIRST_LOGIN, email={}", user.getEmail());

//            auditLogService.createAuditLogForSelf(AuditActionType.LOGIN_SUCCESS, user);
            return authenticateAndGenerateTokens(user);
        }

//        otpService.generateOtp(user.getEmail(), user.getFirstName());
        appMetrics.incrementOtpSend();
        log.info("OTP sent for 2FA: event=OTP_REQUIRED, email={}", user.getEmail());

        return new LoginResponse.OtpRequired("OTP code sent to your email address.");
    }

    private User authenticateUser(LoginRequest request) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        } catch (AuthenticationException exception) {
            appMetrics.incrementLoginFailure();
            boolean blocked = loginRateLimitService.incrementFailedAttempts(request.email());
            if (blocked) {
                appMetrics.incrementAccountLocked();
                sendAccountLockedNotification(request.email());
                throw new LoginLimitException();
            }
            throw exception;
        }

        if (!(authentication.getPrincipal() instanceof UserDetails userDetails)) {
            appMetrics.incrementLoginFailure();

            loginRateLimitService.incrementFailedAttempts(request.email());
            throw new AuthException(AuthErrorType.INVALID_CREDENTIALS);
        }

        return userService.findByEmail(userDetails.getUsername());
    }

    private void sendAccountLockedNotification(String email) {
        try {
            User user = userService.findByEmail(email);
            long blockMinutes = loginRateLimitProperties.getBlockDuration().toMinutes();
            String resetUrl = tokenService.generateToken(user);

            Map<String, String> params = Map.of(
                    "firstName", user.getFirstName(),
                    "maxAttempts", String.valueOf(loginRateLimitProperties.getMaxAttempts()),
                    "blockMinutes", String.valueOf(blockMinutes),
                    "resetUrl", resetUrl
            );

            //     notificationService.notify(NotificationType.ACCOUNT_LOCKED_EMAIL, user.getEmail(), params);

            log.info("Account locked notification sent: email={}, params={}", user.getEmail(), params);
        } catch (Exception e) {
            log.warn("Failed to send account locked notification for identifier: {}", email);
        }
    }

    private void checkDuplicateUser(String email, String phoneNumber) {
        if (userRepository.existsByEmail(email)) {
            log.info("User creation rejected: event=EMAIL_ALREADY_EXISTS, email={}", email);
            throw new UserException(UserErrorType.EMAIL_ALREADY_EXISTS);
        }
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            log.info("User creation rejected: event=PHONE_ALREADY_EXISTS, phone={}", phoneNumber);
            throw new UserException(UserErrorType.PHONE_ALREADY_EXISTS);
        }
    }


    private LoginResponse.Authenticated authenticateAndGenerateTokens(User user) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String tokenId = UUID.randomUUID().toString();
        String accessToken = jwtService.generateAccessToken(userDetails, user.isFirstLogin());
        String refreshToken = jwtService.generateRefreshToken(userDetails, tokenId);
        refreshTokenService.save(user.getEmail(), tokenId);
        long expiresIn = jwtProperties.getAccessTokenExpiry().toSeconds();

        return new LoginResponse.Authenticated(accessToken, refreshToken, expiresIn, user.isFirstLogin());
    }

    @Override
    public LoginResponse.Authenticated refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String token = refreshTokenRequest.refreshToken();

        if (!jwtService.isTokenValid(token)) {
            throw new AuthException(AuthErrorType.INVALID_CREDENTIALS);
        }

        String email = jwtService.getUsernameFromToken(token);
        String oldTokenId = jwtService.extractTokenId(token);

        if (oldTokenId == null) {
            throw new AuthException(AuthErrorType.INVALID_CREDENTIALS);
        }

        String newTokenId = UUID.randomUUID().toString();
        refreshTokenService.rotate(email, oldTokenId, newTokenId);


        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        String newAccessToken = jwtService.generateAccessToken(userDetails, userService.findByEmail(email).isFirstLogin());
        String newRefreshToken = jwtService.generateRefreshToken(userDetails, newTokenId);
        long expiresIn = jwtProperties.getAccessTokenExpiry().toSeconds();

        return new LoginResponse.Authenticated(newAccessToken, newRefreshToken, expiresIn, userService.findByEmail(email).isFirstLogin());

    }

    @Override
    public void logout(RefreshTokenRequest request) {
        String token = request.refreshToken();

        if (jwtService.isTokenValid(token)) {
            String tokenId = jwtService.extractTokenId(token);
            String email = jwtService.getUsernameFromToken(token);

            if (Objects.nonNull(tokenId)) {
                refreshTokenService.revoke(email, tokenId);
                log.info("User logged out successfully: {}", email);
            }
        }
    }


}
