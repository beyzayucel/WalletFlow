package com.walletflow.auth.dto.response;

public sealed interface LoginResponse {

    record Authenticated(String type,
                         String accessToken,
                         String refreshToken,
                         String tokenType,
                         long expiresIn,
                         boolean firstLogin) implements LoginResponse{
        public Authenticated(String accessToken, String refreshToken, long expiresIn, boolean firstLogin){
            this("AUTHENTICATED", accessToken, refreshToken, "Bearer", expiresIn, firstLogin);
        }
    }

    record OtpRequired(String type,
                       String message) implements LoginResponse{
        public OtpRequired(String message){
            this("OTP_REQUIRED", message);
        }
    }

}
