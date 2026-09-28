package com.walletflow.auth.passwordreset.entity;

import com.walletflow.common.entity.BaseToken;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "verification_tokens")
@SuperBuilder
@NoArgsConstructor
public class PasswordResetToken extends BaseToken {
}
