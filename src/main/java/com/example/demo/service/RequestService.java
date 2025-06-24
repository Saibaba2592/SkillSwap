package com.example.demo.service;

import com.example.demo.entity.Request;
import com.example.demo.entity.User;
import com.example.demo.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RequestService {

    @Autowired
    public RequestRepository requestRepository;

    public Request sendRequest(User sender, User receiver) {
        Request req = new Request();
        req.setSender(sender);
        req.setReceiver(receiver);
        req.setStatus("PENDING");
        req.setTimestamp(java.time.LocalDateTime.now());
        return requestRepository.save(req);
    }

    public Request respondRequest(Request request, String status) {
        request.setStatus(status);
        return requestRepository.save(request);
    }
}
