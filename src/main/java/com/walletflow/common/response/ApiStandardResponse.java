package com.walletflow.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiStandardResponse<T>(
        boolean succes,
        T data,
        String message,
        ErrorDetail error
) {
    public static <T> ApiStandardResponse<T> of(T data) {
        return new ApiStandardResponse<>(true, data, null, null);
    }

    public static ApiStandardResponse message(String message) {
        return new ApiStandardResponse<>(true, null, message, null);
    }

    public static ApiStandardResponse<Void> error(ErrorDetail errorDetail) {
        return new ApiStandardResponse<>(false, null, null, errorDetail);
    }

}
