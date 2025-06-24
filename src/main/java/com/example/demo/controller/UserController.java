package com.example.demo.controller;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;


@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            System.out.println("⚠ NO userDetails found.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        System.out.println("✅ Logged in user: " + userDetails.getUsername());
        return ResponseEntity.ok(userService.findByUsername(userDetails.getUsername()).orElseThrow());
    }




    @PutMapping("/update")
    @PreAuthorize("hasRole('USER','ADMIN')")
    public User updateProfile(Principal principal, @RequestBody User updated) {
        User user = userService.findByUsername(principal.getName()).orElseThrow();
        user.setBio(updated.getBio());
        user.setAvailability(updated.getAvailability());
        user.setLocation(updated.getLocation());
        return userService.save(user);
    }
    
    @GetMapping("/others")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> getOtherUsers(Principal principal) {
        User currentUser = userRepository.findByUsername(principal.getName())
                                         .orElseThrow(() -> new RuntimeException("User not found"));

        List<UserDto> users = userRepository.findByRole("ROLE_USER").stream()
            .filter(u -> !u.getId().equals(currentUser.getId()))
            .map(u -> new UserDto(
                u.getUsername(),
                u.getEmail(),
                u.getBio(),
                u.getLocation(),
                u.getAvailability(),
                u.getRole() // ✅ include role
            ))
            .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

}

