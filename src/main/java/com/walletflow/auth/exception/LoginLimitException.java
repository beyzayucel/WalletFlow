package com.walletflow.auth.exception;

import com.walletflow.common.exception.BaseException;

public class LoginLimitException extends BaseException {

    public LoginLimitException() {
        super(AuthErrorType.ACCOUNT_LOCKED);
    }

}
