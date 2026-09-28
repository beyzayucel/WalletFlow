package com.walletflow.auth.exception;

import com.walletflow.common.exception.BaseException;
import com.walletflow.user.exception.UserErrorType;

public class UserException extends BaseException {

    public UserException(UserErrorType errorType) {
        super(errorType);
    }

}
