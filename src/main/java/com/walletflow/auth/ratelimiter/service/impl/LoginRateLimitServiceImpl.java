package com.walletflow.auth.ratelimiter.service.impl;

import com.walletflow.auth.ratelimiter.config.LoginRateLimitProperties;
import com.walletflow.auth.ratelimiter.keygenerator.RateLimitKeyGenerator;
import com.walletflow.auth.ratelimiter.service.LoginBlockListService;
import com.walletflow.auth.ratelimiter.service.LoginRateLimitService;
import com.walletflow.auth.verificationtoken.utils.HashUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginRateLimitServiceImpl implements LoginRateLimitService {

    private final RateLimitKeyGenerator rateLimitKeyGenerator;
    private final RedisTemplate<String, String> redisTemplate;
    private final LoginRateLimitProperties loginRateLimitProperties;
    private final LoginBlockListService loginBlockListService;


    @Override
    public boolean incrementFailedAttempts(String email) {
        String hashMail = HashUtils.hash(email);
        String attemptKey = rateLimitKeyGenerator.createAttemptKey(hashMail);
        Long attempts = Optional.ofNullable(redisTemplate.opsForValue().increment(attemptKey)).orElse(0L);

        log.debug("Failed login attempt {} for user: {}", attempts, hashMail);

        initializeExpirationIfNeeded(attemptKey, attempts);

        return handleMaxAttempts(hashMail, attempts);
    }

    private void initializeExpirationIfNeeded(String attemptKey, Long attempts) {
        if (attempts == 1L) {
            redisTemplate.expire(attemptKey, loginRateLimitProperties.getDuration());
        }
    }

    private boolean handleMaxAttempts(String hashMail, Long attempts) {
        if (attempts >= loginRateLimitProperties.getMaxAttempts()) {
            log.warn("User exceeded maximum login attempts ({}). Blocking user: {}", attempts, hashMail);
            loginBlockListService.blockUser(hashMail);
            resetAttemptsByHash(hashMail);
            return true;
        }
        return false;
    }

    public void resetAttemptsByHash(String email) {
        String hashEmail = HashUtils.hash(email);
        String key = rateLimitKeyGenerator.createAttemptKey(hashEmail);

        Optional.ofNullable(redisTemplate.delete(key))
                .filter(Boolean::booleanValue)
                .ifPresent(ignored -> log.debug("Reset login attempts for user: {}", hashEmail));

//        aslında üstteki kısmın yaptığı şey:
//
//        Boolean deleted = redisTemplate.delete(key);
//        if (deleted != null && deleted) {
//            log.debug("Reset login attempts for user: {}", hashMail);
//        }
    }





}
