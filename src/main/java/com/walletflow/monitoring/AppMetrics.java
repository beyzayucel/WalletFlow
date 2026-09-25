package com.walletflow.monitoring;

import org.springframework.stereotype.Component;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
@Component
public class AppMetrics {

    private final Counter loginSuccess;//basarılı login sayısı
    private final Counter loginFailure;//basarısız login sayısı
    private final Counter otpSend;//kaç kere otp gönderilmiş
    private final Counter otpVerifySuccess;//kaç kez doğru otp girilmiş
    private final Counter otpVerifyFailure;//kaç kez yanlış otp girilmiş
    private final Counter passwordResetRequest;//kaç kez şifremi unuttum isteği gelmiş
    private final Counter tokenRefresh;//bir tokenin kaç kere yenilendiği
    private final Counter accountLocked;//hesap kaç kere kilitlendi

    public AppMetrics(MeterRegistry registry) {
        this.loginSuccess = Counter.builder("auth.login.success")
                .description("Successful login count")
                .register(registry);

        this.loginFailure = Counter.builder("auth.login.failure")
                .description("Failed login count")
                .register(registry);

        this.otpSend = Counter.builder("auth.otp.send")
                .description("OTP send count")
                .register(registry);

        this.otpVerifySuccess = Counter.builder("auth.otp.verify.success")
                .description("Successful OTP verification count")
                .register(registry);

        this.otpVerifyFailure = Counter.builder("auth.otp.verify.failure")
                .description("Failed OTP verification count")
                .register(registry);

        this.passwordResetRequest = Counter.builder("auth.password.reset.request")
                .description("Password reset request count")
                .register(registry);

        this.tokenRefresh = Counter.builder("auth.token.refresh")
                .description("Token refresh count")
                .register(registry);

        this.accountLocked = Counter.builder("auth.account.locked")
                .description("Account locked by rate limiter count")
                .register(registry);
    }

    public void incrementLoginSuccess() {

        loginSuccess.increment();
    }

    public void incrementLoginFailure() {

        loginFailure.increment();
    }

    public void incrementOtpSend() {
        otpSend.increment();
    }

    public void incrementOtpVerifySuccess() {
        otpVerifySuccess.increment();
    }

    public void incrementOtpVerifyFailure() {
        otpVerifyFailure.increment();
    }

    public void incrementPasswordResetRequest() {
        passwordResetRequest.increment();
    }

    public void incrementTokenRefresh() {
        tokenRefresh.increment();
    }

    public void incrementAccountLocked() {
        accountLocked.increment();
    }
}

