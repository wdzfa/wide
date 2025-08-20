package com.wdzfa.wide.service;

import com.wdzfa.wide.model.User;
import com.wdzfa.wide.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User register(User request){
        return userRepository.save(request);
    }

    public Optional<User> findByName(String name) {
        return userRepository.findUserByName(name);
    }

}
