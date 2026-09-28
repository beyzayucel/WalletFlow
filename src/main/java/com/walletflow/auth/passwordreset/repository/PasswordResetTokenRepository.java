package com.walletflow.auth.passwordreset.repository;

import com.walletflow.auth.passwordreset.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {
    void deleteByUserId(UUID userId);
    Optional<PasswordResetToken> findByToken(String token);
}
