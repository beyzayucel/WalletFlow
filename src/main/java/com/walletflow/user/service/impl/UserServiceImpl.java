package com.walletflow.user.service.impl;

import com.walletflow.user.entity.User;
import com.walletflow.user.exception.UserNotFoundException;
import com.walletflow.user.repository.UserRepository;
import com.walletflow.user.service.UserService;
import com.walletflow.user.utils.EmailNormalizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User findByEmail(String email) {
        String normalize = EmailNormalizer.normalize(email);
        return userRepository.findByEmail(normalize).orElseThrow(UserNotFoundException::new);
    }

    @Override
    @Transactional
    public void updateLastLogin(User user) {
        user.setLastLoginAt(Instant.now());
        userRepository.save(user);
    }

}
