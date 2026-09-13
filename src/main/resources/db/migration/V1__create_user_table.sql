CREATE TABLE users
(
    -- BaseEntity alanları
    id             VARCHAR(36)  NOT NULL,
    created_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by     VARCHAR(255) NULL,
    updated_by     VARCHAR(255) NULL,

    -- SoftDeletableEntity alanları
    deleted        TINYINT(1) NOT NULL DEFAULT 0,
    deleted_at     TIMESTAMP NULL DEFAULT NULL,

    -- User entity alanları
    email          VARCHAR(320) NOT NULL,
    password       VARCHAR(255) NULL,
    first_name     VARCHAR(50)  NOT NULL,
    last_name      VARCHAR(50)  NOT NULL,
    phone_number   VARCHAR(16)  NOT NULL,
    email_verified TINYINT(1) NOT NULL DEFAULT 0,
    enabled        TINYINT(1) NOT NULL DEFAULT 0,
    first_login    TINYINT(1) NOT NULL DEFAULT 1,
    role           VARCHAR(20)  NOT NULL DEFAULT 'USER',

    -- Kısıtlamalar (Constraints) ve İndeksler
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uk_users_email UNIQUE (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_users_deleted ON users (deleted);