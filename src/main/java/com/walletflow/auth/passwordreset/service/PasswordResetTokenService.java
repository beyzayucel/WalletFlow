package com.walletflow.auth.passwordreset.service;

import com.walletflow.auth.passwordreset.config.PasswordResetProperties;
import com.walletflow.auth.passwordreset.entity.PasswordResetToken;
import com.walletflow.auth.passwordreset.exception.InvalidPasswordResetTokenException;
import com.walletflow.auth.passwordreset.repository.PasswordResetTokenRepository;
import com.walletflow.auth.service.TokenService;
import com.walletflow.auth.verificationtoken.exception.InvalidVerificationTokenException;
import com.walletflow.auth.verificationtoken.utils.HashUtils;
import com.walletflow.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PasswordResetTokenService implements TokenService {

    private final PasswordResetProperties properties;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public String generateToken(User user) {
        passwordResetTokenRepository.deleteByUserId(user.getId());

        String token = UUID.randomUUID().toString();
        String hash = HashUtils.hash(token);

        PasswordResetToken verificationToken = PasswordResetToken.builder()
                .user(user)
                .token(hash)
                .expiryDate(Instant.now().plus(properties.getExpireDuration()))
                .build();

        passwordResetTokenRepository.save(verificationToken);
        return properties.getUrl() + token;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validateToken(String token) {
        String hash = HashUtils.hash(token);
        return passwordResetTokenRepository.findByToken(hash)
                .map(PasswordResetToken::isValid)
                .orElse(false);
    }

    @Override
    @Transactional
    public User consumeToken(String token) {
        String hash = HashUtils.hash(token);
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(hash).orElseThrow(InvalidPasswordResetTokenException::new);

        if (!resetToken.isValid()) {
            throw new InvalidVerificationTokenException();
        }

        resetToken.setUsed(true);
        return resetToken.getUser();
    }

}
