package com.walletflow.auth.service.impl;

import com.walletflow.auth.dto.request.LoginRequest;
import com.walletflow.auth.dto.request.RegisterRequest;
import com.walletflow.auth.dto.response.LoginResponse;
import com.walletflow.auth.exception.EmailAlreadyExistsException;
import com.walletflow.auth.service.AuthService;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final VerificationTokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final AppMetrics appMetrics;


    @Override
    public void userRegister(RegisterRequest request){
        if(userRepository.existsByEmail(request.email())){
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

//        User user = authenticateUser(loginRequest);




        return null;
    }

//    private User authenticateUser(LoginRequest request) {
//        Authentication authentication;
//        try {
//            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
//        }
//        catch (AuthenticationException exception) {
//            appMetrics.incrementLoginFailure();
//            boolean blocked = loginRateLimitService.incrementFailedAttempts(request.identifier());
//            if (blocked) {
//                appMetrics.incrementAccountLocked();
//
//                sendAccountLockedNotification(request.identifier());
//                throw new LoginLimitException();
//            }
//            throw exception;
//        }
//
//        if (!(authentication.getPrincipal() instanceof UserDetails userDetails)) {
//            appMetrics.incrementLoginFailure();
//
//            loginRateLimitService.incrementFailedAttempts(request.identifier());
//            throw new AuthException(AuthErrorType.INVALID_CREDENTIALS);
//        }
//
//        return userService.findByEmail(userDetails.getUsername());
//    }

}
