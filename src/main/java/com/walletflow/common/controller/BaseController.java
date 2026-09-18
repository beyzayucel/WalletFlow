package com.walletflow.common.controller;

import com.walletflow.common.response.ApiStandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class BaseController {

    protected <T> ResponseEntity<ApiStandardResponse<T>> ok(T data) {
        return ResponseEntity.ok(ApiStandardResponse.of(data));
    }

    protected ResponseEntity<ApiStandardResponse<Void>> ok() {
        return ResponseEntity.ok(ApiStandardResponse.message("OK"));
    }

    protected <T> ResponseEntity<ApiStandardResponse<T>> created(T data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiStandardResponse.of(data));
    }

    protected ResponseEntity<ApiStandardResponse<Void>> created() {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiStandardResponse.message("Created"));
    }

    protected ResponseEntity<Void> noContent() {
        return ResponseEntity.noContent().build();
    }

}
