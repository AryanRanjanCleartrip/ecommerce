package com.cleartrip.ecommerce.controller;

import com.cleartrip.ecommerce.model.User;
import com.cleartrip.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }


    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestParam String name, @RequestParam String password) {
        return userService.login(name, password)
                .map(user -> ResponseEntity.ok().body((Object) user))
                .orElse(ResponseEntity.badRequest().body(Map.of("message", "Invalid credentials")));
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
} 