CREATE TABLE verification_tokens
(
    -- BaseEntity alanları
    id          VARCHAR(36)  NOT NULL,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by  VARCHAR(255) NULL,
    updated_by  VARCHAR(255) NULL,

    -- VerificationToken entity alanları
    token       VARCHAR(255) NOT NULL,
    user_id     VARCHAR(36)  NOT NULL,
    expiry_date TIMESTAMP    NOT NULL,
    used        TINYINT(1)   NOT NULL DEFAULT 0,

    -- Kısıtlamalar (Constraints) ve İndeksler
    CONSTRAINT pk_verification_tokens PRIMARY KEY (id),
    CONSTRAINT uk_verification_tokens_token UNIQUE (token),
    CONSTRAINT fk_verification_tokens_users FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_verification_tokens_token ON verification_tokens (token);