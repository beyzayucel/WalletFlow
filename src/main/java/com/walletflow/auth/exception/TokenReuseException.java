package com.walletflow.auth.exception;

import com.walletflow.common.exception.BaseException;

public class TokenReuseException extends BaseException {

    public TokenReuseException() {
        super(AuthErrorType.TOKEN_REUSE_DETECTED);
    }
}
