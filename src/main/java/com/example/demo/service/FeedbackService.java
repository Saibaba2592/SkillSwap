package com.example.demo.service;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.entity.Feedback;
import com.example.demo.repository.Feedbackrepository;

@Service
public class FeedbackService {

    @Autowired
    private Feedbackrepository feedbackRepository;

    public Feedback giveFeedback(Feedback feedback) {
        feedback.setTimestamp(LocalDateTime.now());
        return feedbackRepository.save(feedback);
    }
}
