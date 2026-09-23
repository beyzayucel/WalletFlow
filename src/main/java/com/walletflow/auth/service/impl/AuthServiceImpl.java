package com.walletflow.auth.service.impl;

import com.walletflow.auth.dto.request.LoginRequest;
import com.walletflow.auth.dto.request.RegisterRequest;
import com.walletflow.auth.dto.response.LoginResponse;
import com.walletflow.auth.exception.EmailAlreadyExistsException;
import com.walletflow.auth.service.AuthService;
import com.walletflow.auth.verificationtoken.service.VerificationTokenService;
import com.walletflow.user.entity.Role;
import com.walletflow.user.entity.User;
import com.walletflow.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final VerificationTokenService tokenService;

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
        return null;
    }


}
