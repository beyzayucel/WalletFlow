package com.walletflow.auth.verificationtoken.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.password-reset")
public class VerificationTokenProperties {
    private String url;
    private Duration expireDuration;
}
