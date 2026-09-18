package com.walletflow.auth.exception;

import com.walletflow.common.exception.BaseException;

public class EmailAlreadyExistsException extends BaseException {

    public EmailAlreadyExistsException() {
        super(AuthErrorType.EMAIL_ALREADY_EXISTS);
    }
}
