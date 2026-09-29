package com.walletflow.user.entity;

import com.walletflow.common.entity.SoftDeletableEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

import java.time.Instant;


@Getter
@Setter
@Entity
@NoArgsConstructor
@SuperBuilder
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "uk_users_email", columnNames = "email"),
        @UniqueConstraint(name = "uk_users_phone_number", columnNames = "phoneNumber")
})
@SQLDelete(sql = "UPDATE users SET deleted = 1, deleted_at = NOW() WHERE id = ?")
public class User extends SoftDeletableEntity {

    @Column(nullable = false)
    private String email;

    @Column
    private String password;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, length = 16)
    private String phoneNumber;

    @Builder.Default
    @Column(nullable = false)
    private boolean emailVerified = false;

    @Builder.Default
    @Column(nullable = false)
    private boolean enabled = false;

    @Builder.Default
    @Column(nullable = false)
    private boolean firstLogin = true;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    private Instant lastLoginAt;

}
