package com.walletflow.auth.passwordreset.exception;

import com.walletflow.common.exception.BaseException;

public class InvalidPasswordResetTokenException extends BaseException {

    public InvalidPasswordResetTokenException(){
        super(PasswordResetErrorType.INVALID_TOKEN);
    }

}
