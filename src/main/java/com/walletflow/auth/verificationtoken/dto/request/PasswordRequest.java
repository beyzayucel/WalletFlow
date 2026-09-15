package com.walletflow.auth.verificationtoken.dto.request;

import com.walletflow.common.validation.Password;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record PasswordRequest(
        @NotBlank(message = "{validation.token.required}")
        String token,

        @Schema(description = "New Password", example = "as456Asdf.")
        @NotBlank(message = "{validation.new.password.required}")
        @Password
        String password

) {
}
