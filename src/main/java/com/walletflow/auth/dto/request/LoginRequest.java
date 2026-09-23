package com.walletflow.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "{validation.email.required}")
        @Email(message = "{validation.email.format}")
        String email,

        @Schema(description = "Password", example = "ChangeMe!2026")
        @NotBlank(message = "{validation.password.required}")
        String password
) {}
