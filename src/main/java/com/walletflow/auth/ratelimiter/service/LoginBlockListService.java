package com.walletflow.auth.ratelimiter.service;

public interface LoginBlockListService {

    void blockUser(String hashMail);

}
