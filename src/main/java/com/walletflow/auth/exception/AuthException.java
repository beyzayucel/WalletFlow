package com.walletflow.auth.exception;

import com.walletflow.common.exception.BaseException;

public class AuthException extends BaseException {

    public AuthException(AuthErrorType errorType) {
        super(errorType);
    }

    public AuthException(AuthErrorType errorType, Throwable cause) {
        super(errorType, cause);
    }
}
