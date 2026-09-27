package com.walletflow.auth.ratelimiter.service.impl;

import com.walletflow.auth.ratelimiter.keygenerator.RateLimitKeyGenerator;
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


    @Override
    public boolean incrementFailedAttempts(String email) {
        String hashMail = HashUtils.hash(email);
        String attemptKey = rateLimitKeyGenerator.createAttemptKey(hashMail);
        Long attempts = Optional.ofNullable(redisTemplate.opsForValue().increment(attemptKey)).orElse(0L);

        log.debug("Failed login attempt {} for user: {}", attempts, hashMail);





        return false;
    }

    private void initializeExpirationIfNeeded(String attemptKey, Long attempts){
        if (attempts == 1L){
//            redisTemplate.expire(attemptKey, loginRateLimitProperties.getDuration());
        }
    }
}
