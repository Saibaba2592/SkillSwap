package com.example.demo.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.FeedbackDTO;
import com.example.demo.entity.User;
import com.example.demo.repository.Feedbackrepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.EmailService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService mailService;
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> allUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/block/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public String blockUser(@PathVariable String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        user.setRole("BLOCKED");
        userRepository.save(user);
        mailService.sendBlockNotification(user.getEmail(), user.getUsername());
        return "User blocked";
    }

    @PostMapping("/approve/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public String approveUser(@PathVariable String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole("ROLE_USER");
        userRepository.save(user);

        mailService.sendApprovalNotification(user.getEmail(), user.getUsername());

        return "User approved and notified via email";
    }

    

}