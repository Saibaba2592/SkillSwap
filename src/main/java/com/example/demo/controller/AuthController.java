package com.example.demo.controller;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.EmailService;
import com.example.demo.service.OtpService;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import com.example.demo.dto.*;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	private  UserService userService;
	@Autowired
    private  PasswordEncoder passwordEncoder;
	@Autowired
	private AuthenticationManager authenticationManager;
    @Autowired private OtpService otpService;
    @Autowired private EmailService emailService;
    @Autowired private UserRepository userRepo;




    

        

        @PostMapping("/register")
        public ResponseEntity<?> register(@RequestBody RegistrationRequest req) {
            User user = new User();
            user.setUsername(req.getUsername());
            user.setEmail(req.getEmail());
            user.setPassword(passwordEncoder.encode(req.getPassword()));
            user.setBio(req.getBio());
            user.setAvailability(req.getAvailability());
            user.setLocation(req.getLocation());

            // ✅ Ensure proper role format
            if (!req.getRole().startsWith("ROLE_")) {
                user.setRole("ROLE_" + req.getRole().toUpperCase());
            } else {
                user.setRole(req.getRole().toUpperCase());
            }

            User savedUser = userService.register(user);
            emailService.sendRegistrationConfirmation(user.getEmail());

            Map<String, Object> response = new HashMap<>();
            response.put("username", savedUser.getUsername());
            response.put("email", savedUser.getEmail());
            response.put("message", "User registered successfully!");
            return ResponseEntity.ok(response);
        }

        @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
            try {
                Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);

                HttpSession session = httpRequest.getSession(true);
                session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                        SecurityContextHolder.getContext());

                User user = userRepo.findByUsername(request.getUsername()).orElseThrow();
                return ResponseEntity.ok(user);
            } catch (Exception e) {
                return ResponseEntity.status(401).body("Invalid username or password");
            }
        }











    @PostMapping("/forgot-password")
    @PreAuthorize("hasAnyRole ('ADMIN','USER')")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> req) {
        String email = req.get("email");
        if (!userRepo.findAll().stream().anyMatch(u -> u.getEmail().equals(email)))
            return ResponseEntity.badRequest().body("Email not registered.");

        String otp = otpService.generateAndSaveOtp(email);
        emailService.sendOtp(email, otp);
        return ResponseEntity.ok("OTP sent to email.");
    }

    @PostMapping("/reset-password")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> req) {
        String email = req.get("email");
        String otp = req.get("otp");
        String newPassword = req.get("newPassword");

        if (email == null || otp == null || newPassword == null || newPassword.isBlank()) {
            return ResponseEntity.badRequest().body("Email, OTP, and new password must be provided.");
        }

        if (!otpService.verifyOtp(email, otp)) {
            return ResponseEntity.badRequest().body("Invalid or expired OTP.");
        }

        User user = userRepo.findAll().stream()
            .filter(u -> u.getEmail().equals(email))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("User not found."));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);
        otpService.clearOtp(email);

        return ResponseEntity.ok("Password reset successfully.");
    }


    
    

}


