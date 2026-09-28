package com.walletflow.auth.passwordreset.service;

import com.walletflow.user.entity.User;

public interface PasswordResetTokenService {

    String passwordResetTokenService(User user);

}
