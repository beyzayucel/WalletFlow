package com.walletflow.common.exception;

import com.walletflow.common.response.ApiStandardResponse;
import com.walletflow.common.response.ErrorDetail;
import com.walletflow.common.web.RequestIdFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiStandardResponse<Void>> handleBaseException(BaseException ex, HttpServletRequest request) {
        String message = resolveMessage(ex.getErrorType());
        return buildResponse(ex.getErrorType(), message, request);
    }

    private String resolveMessage(BaseErrorType errorType, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(errorType.getMessageKey(), args, locale);
    }

    private ResponseEntity<ApiStandardResponse<Void>> buildResponse(BaseErrorType errorType, String message, HttpServletRequest request) {
        ErrorDetail errorDetail = ErrorDetail.builder(
                        errorType.getHttpStatus().value(),
                        errorType.getCode(),
                        message,
                        request.getRequestURI())
                .requestId(MDC.get(RequestIdFilter.REQUEST_ID_MDC_KEY))
                .build();


        return ResponseEntity
                    .status(errorType.getHttpStatus())
                    .body(ApiStandardResponse.error(errorDetail));
    }

}
