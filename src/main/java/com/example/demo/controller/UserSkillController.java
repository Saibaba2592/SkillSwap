package com.example.demo.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.SkillAddRequest;
import com.example.demo.dto.UserSkillDto;
import com.example.demo.entity.User;
import com.example.demo.entity.UserSkill;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserSkillRepository;

import jakarta.validation.Valid;

@RestController
@Validated
@RequestMapping("/api/user/skills")
public class UserSkillController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSkillRepository userSkillRepository;

    // ✅ Add a skill
    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> addSkill(Principal principal, @Valid @RequestBody SkillAddRequest req) {
        if (req.getSkillName() == null || req.getSkillName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Skill name cannot be null or empty");
        }

        User user = userRepository.findByUsername(principal.getName()).orElseThrow();

        boolean exists = userSkillRepository.findByUserAndType(user, req.getType()).stream()
            .anyMatch(us -> us.getSkillName().equalsIgnoreCase(req.getSkillName().trim()));

        if (exists) {
            return ResponseEntity.badRequest().body("Skill already exists with the same type.");
        }

        UserSkill userSkill = new UserSkill();
        userSkill.setUser(user);
        userSkill.setSkillName(req.getSkillName().trim());
        userSkill.setType(req.getType());

        return ResponseEntity.ok(userSkillRepository.save(userSkill));
    }

    // ✅ Remove a skill
    @DeleteMapping("/remove")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> removeSkill(Principal principal,
                                         @RequestParam String skillName,
                                         @RequestParam String type) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();

        UserSkill userSkill = userSkillRepository.findByUserAndType(user, type).stream()
            .filter(us -> us.getSkillName().equalsIgnoreCase(skillName.trim()))
            .findFirst()
            .orElse(null);

        if (userSkill == null) {
            return ResponseEntity.badRequest().body("Skill not found for user!");
        }

        userSkillRepository.delete(userSkill);
        return ResponseEntity.ok("Skill removed!");
    }

    // ✅ Get user skills
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<UserSkillDto>> getUserSkills(Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username).orElseThrow();

        List<UserSkill> skills = userSkillRepository.findByUser(user);
        List<UserSkillDto> dtos = skills.stream()
            .map(skill -> new UserSkillDto(skill.getSkillName(), skill.getType()))
            .toList();

        return ResponseEntity.ok(dtos);
    }
}
