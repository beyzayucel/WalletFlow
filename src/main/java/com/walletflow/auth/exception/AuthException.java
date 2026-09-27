package com.walletflow.auth.exception;

import com.walletflow.common.exception.BaseException;

public class AuthException extends BaseException {

    public AuthException() {
        super(AuthErrorType.EMAIL_NOT_VERIFIED);
    }
}
