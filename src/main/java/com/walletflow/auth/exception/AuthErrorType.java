package com.walletflow.auth.exception;

import com.walletflow.common.exception.BaseErrorType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorType implements BaseErrorType {

    EMAIL_ALREADY_EXISTS("error.email.already.exists", HttpStatus.CONFLICT);

    private final String messageKey;
    private final HttpStatus httpStatus;


    @Override
    public String getCode() {
        return name();
    }
}
