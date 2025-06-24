package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.entity.UserSkill;
import com.example.demo.repository.UserSkillRepository;

@Service
public class UserSkillService {
    @Autowired
    private UserSkillRepository userSkillRepository;

    public List<UserSkill> getSkillsByUserAndType(User user, String type) {
        return userSkillRepository.findByUserAndType(user, type);
    }
}
