package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.User;
import com.example.demo.entity.UserSkill;

public interface UserSkillRepository extends JpaRepository<UserSkill, Long>{
	List<UserSkill> findByUserAndType(User user, String type);
    List<UserSkill> findByTypeAndSkillName(String type, String skillName);
	List<UserSkill> findByUser(User user);
}
