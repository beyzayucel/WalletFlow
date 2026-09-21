package com.walletflow.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorDetail(
        int status,
        String code,
        String message,
        String path,
        Instant timestamp,
        List<FieldError> fieldErrors,
        String requestId,
        Long remainingTime,
        Integer remainingAttempts
) {
    public static ErrorDetailBuilder builder(int status, String code, String message, String path) {
        return new ErrorDetailBuilder(status, code, message, path);
    }
}
