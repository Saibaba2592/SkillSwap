package com.example.demo.dto;

public class UserSkillDto {
    private String skillName;
    private String type;

    public UserSkillDto() {}

    public UserSkillDto(String skillName, String type) {
        this.skillName = skillName;
        this.type = type;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
