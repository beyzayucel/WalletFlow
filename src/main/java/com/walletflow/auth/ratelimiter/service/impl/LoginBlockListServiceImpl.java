package com.walletflow.auth.ratelimiter.service.impl;

import com.walletflow.auth.ratelimiter.config.LoginRateLimitProperties;
import com.walletflow.auth.ratelimiter.keygenerator.RateLimitKeyGenerator;
import com.walletflow.auth.ratelimiter.service.LoginBlockListService;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

@RequiredArgsConstructor
@Server
@Slf4j
public class LoginBlockListServiceImpl implements LoginBlockListService {

    private final LoginRateLimitProperties loginRateLimitProperties;
    private final RedisTemplate<String, String> redisTemplate;
    private final RateLimitKeyGenerator keyGenerator;

    @Override
    public void blockUser(String hashedIdentifier) {
        String blockKey = keyGenerator.createBlockKey(hashedIdentifier);
        redisTemplate.opsForValue().set(blockKey, Boolean.TRUE.toString(), loginRateLimitProperties.getBlockDuration());
        log.warn("User has been blocked for {} after exceeding the maximum number of attempts. User: {}", loginRateLimitProperties.getBlockDuration(), hashedIdentifier);
    }
}
