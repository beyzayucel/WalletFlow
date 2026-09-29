package com.walletflow.security.exception;

import com.walletflow.common.exception.BaseException;

public class JwtTokenException extends BaseException {

    public JwtTokenException(JwtErrorType errorType) {
        super(errorType);
    }

    public JwtTokenException(JwtErrorType errorType, Throwable cause) {
        super(errorType, cause);
    }
}

