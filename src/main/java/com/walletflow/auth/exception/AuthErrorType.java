package com.walletflow.auth.exception;

import com.walletflow.common.exception.BaseErrorType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorType implements BaseErrorType {

    EMAIL_ALREADY_EXISTS("error.email.already.exists", HttpStatus.CONFLICT),
    USER_NOT_FOUND("error.user.not.found", HttpStatus.NOT_FOUND),
    INVALID_PASSWORD("error.invalid.password", HttpStatus.UNAUTHORIZED),
    EMAIL_NOT_VERIFIED("error.email.not.verified", HttpStatus.FORBIDDEN),
    ACCOUNT_LOCKED("error.account.locked", HttpStatus.TOO_MANY_REQUESTS);

    private final String messageKey;
    private final HttpStatus httpStatus;


    @Override
    public String getCode() {
        return name();
    }
}
