package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Feedback;
import com.example.demo.entity.User;

public interface Feedbackrepository extends JpaRepository<Feedback, Long>{
	List<Feedback> findByReceiver(User receiver);
    List<Feedback> findBySender(User sender);
}
