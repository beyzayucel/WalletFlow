package com.walletflow.auth.verificationtoken.service;

import com.walletflow.auth.service.TokenService;
import com.walletflow.auth.verificationtoken.config.VerificationTokenProperties;
import com.walletflow.auth.verificationtoken.dto.request.PasswordRequest;
import com.walletflow.auth.verificationtoken.entity.VerificationToken;
import com.walletflow.auth.verificationtoken.exception.InvalidVerificationTokenException;
import com.walletflow.auth.verificationtoken.repository.VerificationTokenRepository;
import com.walletflow.auth.verificationtoken.utils.HashUtils;
import com.walletflow.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class VerificationTokenService implements TokenService {

    private final VerificationTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenProperties properties;

    @Override
    public String generateToken(User user) {
        String token = UUID.randomUUID().toString();
        String hash = HashUtils.hash(token);
        VerificationToken verificationToken = VerificationToken.builder()
                .user(user)
                .token(hash)
                .expiryDate(Instant.now().plus(properties.getExpireDuration()))
                .build();

        tokenRepository.save(verificationToken);
        return properties.getUrl() + token;
    }

    @Override
    @Transactional
    public boolean validateToken(String token) {
        String hash = HashUtils.hash(token);
        Optional<VerificationToken> optToken = tokenRepository.findByToken(hash);

        if (optToken.isPresent() && optToken.get().isValid()) {
            optToken.get().getUser().setEmailVerified(true);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public User consumeToken(String token) {
        String hash = HashUtils.hash(token);
        VerificationToken verificationToken = tokenRepository.findByToken(hash).orElseThrow(InvalidVerificationTokenException::new);

        if (!verificationToken.isValid()) {
            throw new InvalidVerificationTokenException();
        }

        verificationToken.setUsed(true);
        return verificationToken.getUser();
    }


    @Transactional
    public void completeRegistration(PasswordRequest request) {
        User user = consumeToken(request.token());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEnabled(true);
    }

}
