package com.cleartrip.ecommerce.service;

import com.cleartrip.ecommerce.model.User;
import com.cleartrip.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // dont add duplicate users
    // create user
    public User createUser(User user) {
        // Check for duplicate users before saving
        if (userRepository.findByName(user.getName()).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }
        return userRepository.save(user);
    }

    // login user
    //throw exception here
    public Optional<User> login(String name, String password) {
        Optional<User> user = userRepository.findByName(name);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return user;
        }
        return Optional.empty();
    }

    // get user by id
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
} 