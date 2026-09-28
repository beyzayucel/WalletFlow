package com.walletflow.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseException extends RuntimeException {

    private final transient BaseErrorType errorType;

    protected BaseException(BaseErrorType errorType) {
        super(errorType.getMessageKey());
        this.errorType = errorType;
    }

    protected BaseException(BaseErrorType errorType, Throwable cause) {
        super(errorType.getMessageKey(), cause);
        this.errorType = errorType;
    }

    public BaseException() {
    }

    public HttpStatus getStatus() {
        return errorType.getHttpStatus();
    }

    public String getCode() {
        return errorType.getCode();
    }

}
