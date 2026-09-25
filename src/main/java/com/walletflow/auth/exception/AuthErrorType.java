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
    INVALID_PASSWORD("error.invalid.password", HttpStatus.UNAUTHORIZED);


    private final String messageKey;
    private final HttpStatus httpStatus;


    @Override
    public String getCode() {
        return name();
    }
}
