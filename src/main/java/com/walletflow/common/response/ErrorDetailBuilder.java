package com.walletflow.common.response;

import org.springframework.validation.FieldError;

import java.time.Instant;
import java.util.List;

public class ErrorDetailBuilder {

    private final int status;
    private final String code;
    private final String message;
    private final String path;
    private List<FieldError> fieldErrors;
    private String requestId;
    private Long remainingTime;
    private Integer remainingAttempts;

    public ErrorDetailBuilder(int status, String code, String message, String path) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.path = path;
    }

    public ErrorDetailBuilder fieldErrors(List<FieldError> fieldErrors) {
        this.fieldErrors = fieldErrors;
        return this;
    }

    public ErrorDetailBuilder requestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public ErrorDetailBuilder remainingTime(Long remainingTime) {
        this.remainingTime = remainingTime;
        return this;
    }

    public ErrorDetailBuilder remainingAttempts(Integer remainingAttempts) {
        this.remainingAttempts = remainingAttempts;
        return this;
    }

    public ErrorDetail build() {
        return new ErrorDetail(
                status,
                code,
                message,
                path,
                Instant.now(),
                fieldErrors,
                requestId,
                remainingTime,
                remainingAttempts);
    }
}
