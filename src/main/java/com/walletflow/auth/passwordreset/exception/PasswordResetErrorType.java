package com.walletflow.auth.passwordreset.exception;

import com.walletflow.common.exception.BaseErrorType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PasswordResetErrorType implements BaseErrorType {
    INVALID_TOKEN("error.password.reset.token.invalid", HttpStatus.BAD_REQUEST);

    private final String messageKey;
    private final HttpStatus httpStatus;


    @Override
    public String getCode() {
        return name();
    }
}
