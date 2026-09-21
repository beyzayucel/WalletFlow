package com.walletflow.auth.verificationtoken.service;

import com.walletflow.auth.verificationtoken.dto.request.PasswordRequest;
import com.walletflow.auth.verificationtoken.entity.VerificationToken;
import com.walletflow.auth.verificationtoken.repository.VerificationTokenRepository;
import com.walletflow.auth.verificationtoken.utils.SecurityUtils;
import com.walletflow.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class VerificationTokenService {

    private static final String VERIFICATION_BASE_URL = "http://localhost:8080/api/v1/auth/verify?token=";

    private final VerificationTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    public void generateTokenAndSendEmail(User user) {
        String token = UUID.randomUUID().toString();
        String hash = SecurityUtils.hashToken(token);
        VerificationToken verificationToken = VerificationToken.builder()
                .user(user)
                .token(hash)
                .expiryDate(Instant.now().plus(1, ChronoUnit.DAYS))
                .build();
        tokenRepository.save(verificationToken);

        String verificationTokenUrl = VERIFICATION_BASE_URL + token;

        //Şimdilik -kafkayı ekleyene kadar
        log.info("📧 Mail Gönderildi: Kullanıcı: {}, Link: {}", user.getEmail(), verificationTokenUrl);
    }

    @Transactional
    public boolean isVerifyToken(String token) {
        String hash = SecurityUtils.hashToken(token);
        Optional<VerificationToken> optToken =tokenRepository.findByToken(hash);

        if(optToken.isPresent() && optToken.get().isValid()){
            optToken.get().getUser().setEmailVerified(true);
            return true;
        }
        return false;
    }

    @Transactional
    public void completeRegistration(PasswordRequest request) {
        VerificationToken token = getValidToken(request.token());
        token.setUsed(true);
        activateUser(token.getUser(), request.password());
    }

    private VerificationToken getValidToken(String rawToken) {
        String hash = SecurityUtils.hashToken(rawToken);
        VerificationToken token = tokenRepository.findByToken(hash).orElseThrow(() -> new RuntimeException("Geçersiz token!"));
        if (!token.isValid()) {
            throw new RuntimeException("Bağlantının süresi dolmuş veya zaten kullanılmış!");
        }
        return token;
    }

    private void activateUser(User user, String rawPassword) {
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setEnabled(true);
    }




}