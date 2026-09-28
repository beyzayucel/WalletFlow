package com.walletflow.auth.verificationtoken.exception;

import com.walletflow.common.exception.BaseErrorType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum VerificationTokenErrorType implements BaseErrorType {

    INVALID_TOKEN("error.verification.token.invalid", HttpStatus.BAD_REQUEST);

    private final String messageKey;
    private final HttpStatus httpStatus;

    @Override
    public String getCode() {
        return name();
    }
}