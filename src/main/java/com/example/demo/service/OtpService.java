package com.example.demo.service;

import com.example.demo.entity.OtpEntry;
import com.example.demo.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OtpService {
    @Autowired private OtpRepository otpRepo;
    
    

    public String generateAndSaveOtp(String email) {
        String otp = String.valueOf(100000 + new Random().nextInt(900000));
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(10);
        otpRepo.deleteByEmail(email);
        otpRepo.save(new OtpEntry(email, otp, expiry));
        return otp;
    }

    public boolean verifyOtp(String email, String otp) {
        return otpRepo.findByEmail(email)
            .filter(e -> e.getOtp().equals(otp) && e.getExpirationTime().isAfter(LocalDateTime.now()))
            .isPresent();
    }
    @Transactional
    public void clearOtp(String email) {
        otpRepo.deleteByEmail(email);
    }
}
