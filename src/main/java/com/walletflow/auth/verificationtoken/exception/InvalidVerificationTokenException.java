package com.walletflow.auth.verificationtoken.exception;

import com.walletflow.common.exception.BaseException;

public class InvalidVerificationTokenException extends BaseException {

    public InvalidVerificationTokenException() {
        super(VerificationTokenErrorType.INVALID_TOKEN);
    }
}