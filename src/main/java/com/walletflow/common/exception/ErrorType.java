package com.walletflow.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorType implements BaseErrorType {

    VALIDATION_ERROR("error.validation", HttpStatus.BAD_REQUEST);

    private final String messageKey;
    private final HttpStatus httpStatus;

    public String getCode() {
        return name();
    }
}