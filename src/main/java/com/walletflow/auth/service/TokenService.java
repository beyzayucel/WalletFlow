package com.walletflow.auth.service;

import com.walletflow.user.entity.User;

public interface TokenService<T> {

    String generateToken(User user);

    /**
     * Token'ın geçerli (süresi dolmamış ve kullanılmamış) olup olmadığını doğrular.
     */
    boolean validateToken(String token);

    /**
     Kullanıcı yeni şifresini yazıp formu onayladığında token'ı tüketir (used = true yapar)
     ve token'ın sahibi olan User nesnesini döner.
     */
    User consumeToken(String token);
}
