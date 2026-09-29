package com.walletflow.auth.refreshtoken.service.impl;

import com.walletflow.auth.exception.TokenReuseException;
import com.walletflow.auth.refreshtoken.service.RefreshTokenService;
import com.walletflow.security.config.JwtProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtProperties jwtProperties;
    private static final String REFRESH_TOKEN_KEY_PREFIX = "auth:refresh:";

    @Override
    public void save(String username, String tokenId) {
        String key = createKey(username, tokenId);
        redisTemplate.opsForValue().set(key, "ACTIVE", jwtProperties.getRefreshTokenExpiry());
        log.debug("Refresh token saved to Redis: key={}", key);
    }

    private String createKey(String username, String tokenId) {
        return REFRESH_TOKEN_KEY_PREFIX + username + ":" + tokenId;
    }

    @Override
    public boolean isValid(String username, String tokenId) {
        String key = createKey(username, tokenId);
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    @Override
    public void rotate(String username, String oldTokenId, String newTokenId){
        String oldKey = createKey(username, oldTokenId);
        if(!Boolean.TRUE.equals(redisTemplate.hasKey(oldKey))){
            log.warn("Security Alert: Reused refresh token detected for user: {}. Revoking all sessions!", username);
            revokeAll(username);
            throw new TokenReuseException();
        }
        redisTemplate.delete(oldKey);
        save(username, newTokenId);
        log.info("Refresh token rotated successfully for user: {}", username);
    }

    @Override
    public void revoke(String username, String tokenId){
        String key = createKey(username, tokenId);
        redisTemplate.delete(key);
        log.info("Refresh token revoked for single device: key={}", key);
    }

    @Override
    public void revokeAll(String username) {
        String pattern = REFRESH_TOKEN_KEY_PREFIX + username + ":*";
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
            log.info("All active sessions revoked for user: {}, count={}", username, keys.size());
        }

    }


}
