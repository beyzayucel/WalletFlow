package com.walletflow.user.exception;

import com.walletflow.common.exception.BaseException;

public class UserNotFoundException extends BaseException {

    public UserNotFoundException() {
        super(UserErrorType.USER_NOT_FOUND);
    }
}