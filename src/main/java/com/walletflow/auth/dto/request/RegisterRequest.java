package com.walletflow.auth.dto.request;

import com.walletflow.common.validation.InternationalPhone;
import com.walletflow.common.validation.PersonName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @Schema(description = "User email address", example = "user@walletflow.com")
        @NotBlank(message = "{validation.email.required}")
        @Email(message = "{validation.email.format}")
        @Size(max = 320, message = "{validation.email.size}")
        String email,

        @Schema(description = "First name", example = "Beyza")
        @NotBlank(message = "{validation.firstname.required}")
        @PersonName
        String firstName,

        @Schema(description = "Last name", example = "Yilmaz")
        @NotBlank(message = "{validation.lastname.required}")
        @PersonName
        String lastName,

        @Schema(description = "Phone in E.164 format", example = "+905551234567")
        @NotBlank(message = "{validation.phone.required}")
        @InternationalPhone
        String phoneNumber
) {
}
