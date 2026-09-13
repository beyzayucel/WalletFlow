package com.walletflow.user.service;

import com.walletflow.user.dto.RegisterRequest;
import com.walletflow.user.entity.Role;
import com.walletflow.user.entity.User;
import com.walletflow.user.repository.UserRepository;

public class UserService {

    UserRepository userRepository;

    public void userRegister(RegisterRequest request){
        User user = new User();
        user.setEmail(request.email());
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setPhoneNumber(request.phoneNumber());
        user.setRole(Role.USER);
        userRepository.save(user);
    }


}
