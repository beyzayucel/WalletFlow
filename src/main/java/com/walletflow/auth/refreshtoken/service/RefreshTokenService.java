package com.walletflow.auth.refreshtoken.service;

public interface RefreshTokenService {
    void save(String username, String tokenId);
    boolean isValid(String username, String tokenId);
    void rotate(String username, String oldTokenId, String newTokenId);
    void revoke(String username, String tokenId);
    void revokeAll(String username);
}
