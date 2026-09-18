package com.walletflow.common.exception;

import org.springframework.http.HttpStatus;

public interface BaseErrorType {

    String getMessageKey();

    HttpStatus getHttpStatus();

    String getCode();

}
