package com.walletflow.auth.exception;

import com.walletflow.common.exception.BaseException;

public class InvalidPasswordException extends BaseException {

    public InvalidPasswordException() {
        super(AuthErrorType.INVALID_PASSWORD);
    }
}