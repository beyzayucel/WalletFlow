package com.walletflow.auth.exception;

import com.walletflow.common.exception.BaseErrorType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorType implements BaseErrorType {

    INVALID_CREDENTIALS("error.invalid.credentials", HttpStatus.UNAUTHORIZED),
    EMAIL_ALREADY_EXISTS("error.email.already.exists", HttpStatus.CONFLICT),
    INVALID_PASSWORD("error.invalid.password", HttpStatus.UNAUTHORIZED),
    EMAIL_NOT_VERIFIED("error.email.not.verified", HttpStatus.FORBIDDEN),
    ACCOUNT_LOCKED("error.account.locked", HttpStatus.TOO_MANY_REQUESTS),
    TOKEN_REUSE_DETECTED("error.token.reuse.detected", HttpStatus.UNAUTHORIZED);

    private final String messageKey;
    private final HttpStatus httpStatus;


    @Override
    public String getCode() {
        return name();
    }
}
