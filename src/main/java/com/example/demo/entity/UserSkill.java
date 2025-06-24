package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "user_skills")
public class UserSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @Column(nullable = false)
    private String skillName; // 🔁 Replaces Skill entity

    @Column(nullable = false)
    private String type; // "TEACH" or "LEARN"

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public UserSkill() {}

    public UserSkill(User user, String skillName, String type) {
        this.user = user;
        this.skillName = skillName;
        this.type = type;
    }
}
