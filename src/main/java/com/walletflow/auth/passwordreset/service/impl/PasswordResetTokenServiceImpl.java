package com.walletflow.auth.passwordreset.service.impl;

import com.walletflow.auth.passwordreset.service.PasswordResetTokenService;
import com.walletflow.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    @Override
    public String passwordResetTokenService(User user){

        return null;
    }


}
