package com.walletflow.auth.exception;

import com.walletflow.common.exception.BaseException;

public class UserNotFoundException extends BaseException {

    public UserNotFoundException() {
        super(AuthErrorType.USER_NOT_FOUND);
    }
}