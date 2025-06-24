// com.example.demo.dto.UserDto.java
package com.example.demo.dto;

public class UserDto {
    private String username;
    private String email;
    private String bio;
    private String location;
    private String availability;
    private String role;

    public UserDto(String username, String email, String bio, String location, String availability,String role) {
        this.username = username;
        this.email = email;
        this.bio = bio;
        this.location = location;
        this.availability = availability;
        this.role=role;
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
    
}
