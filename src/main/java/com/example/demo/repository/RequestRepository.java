package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Request;
import com.example.demo.entity.User;

public interface RequestRepository extends JpaRepository<Request, Long>{
	List<Request> findByReceiver(User receiver);
    List<Request> findBySender(User sender);
    List<Request> findBySenderAndReceiver(User sender, User receiver);

}
