package com.walletflow.auth.verificationtoken.repository;

import com.walletflow.auth.verificationtoken.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken,UUID> {

    Optional<VerificationToken> findByToken(String hashToken);

}
