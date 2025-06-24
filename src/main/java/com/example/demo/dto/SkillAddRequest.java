package com.example.demo.dto;

public class SkillAddRequest {
	private String skillName; // e.g. "Java"
    private String type; // "TEACH" or "LEARN"
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
