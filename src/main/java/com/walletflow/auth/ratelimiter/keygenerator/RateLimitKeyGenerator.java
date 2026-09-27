package com.walletflow.auth.ratelimiter.keygenerator;

import com.walletflow.common.constants.RedisKeyConstants;
import org.springframework.stereotype.Component;

@Component
public class RateLimitKeyGenerator {

    /**
     * Hatalı giriş deneme sayacının Redis anahtarı
     */
    public String createAttemptKey(String hashedEmail) {
        return String.format(RedisKeyConstants.LOGIN_ATTEMPTS_KEY, hashedEmail);
    }

    /**
     * Hesabın kilitli olup olmadığını belirten Redis anahtarı
     */
    public String createBlockKey(String hashEmail) {
        return String.format(RedisKeyConstants.LOGIN_BLOCKED_LIST, hashEmail);
    }

    /**
     * Şifre sıfırlama istek sayacının Redis anahtarı
     */
    public String createPasswordResetEmailKey(String hashedEmail) {
        return String.format(RedisKeyConstants.PASSWORD_RESET_EMAIL_REQUESTS_KEY, hashedEmail);
    }

    /**
     * Şifre sıfırlama bekleme süresinin (cooldown) Redis anahtarı
     */
    public String createPasswordResetCooldownKey(String hashedEmail) {
        return String.format(RedisKeyConstants.PASSWORD_RESET_EMAIL_COOLDOWN_KEY, hashedEmail);
    }

}
