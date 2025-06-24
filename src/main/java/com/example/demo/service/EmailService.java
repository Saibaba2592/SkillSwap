package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;



    public void sendOtp(String to, String otp) {
        String subject = "SkillSwap OTP for Password Reset";
        String body = "Dear user,\n\nYour OTP is: " + otp +
                "\nThis OTP is valid for 10 minutes.\n\nRegards,\nSkillSwap Team";
        sendEmail(to, subject, body);
    }

    public void sendRegistrationConfirmation(String to) {
        String subject = "Welcome to SkillSwap!";
        String body = "Dear user,\n\nThank you for registering on SkillSwap.\n\n" +
                "Start learning and teaching today!\n\nRegards,\nSkillSwap Team";
        sendEmail(to, subject, body);
    }

    public void sendRequestAcceptedEmail(String toEmail, String acceptedBy, String recipientName, String meetLink) {
        String subject = "Your SkillSwap Request Has Been Accepted!";
        String body = "Dear " + recipientName + ",\n\n" +
                acceptedBy + " has accepted your SkillSwap session request.\n" +
                "Join the session using this Google Meet link:\n" + meetLink + "\n\n" +
                "All the best for your learning!\n\nRegards,\nSkillSwap Team";
        sendEmail(toEmail, subject, body);
    }

    public void sendFeedbackRequestEmail(String senderEmail, String senderUsername, String receiverUsername) {
        String subject = "Please give your feedback for the session";
        String body = "Dear " + senderUsername + ",\n\n"
                + "Your session with " + receiverUsername + " has concluded.\n"
                + "We would love to hear your feedback.\n\n"
                + "Submit your rating and review at:\n"
                + "http://localhost:8080/api/feedback/giverating\n\n"
                + "Regards,\nSkillSwap Team";

        System.out.println("📨 Preparing to send FEEDBACK email to " + senderEmail);
        sendEmail(senderEmail, subject, body);
    }

    private void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            System.out.println("📤 Sending email — Subject: " + subject + ", To: " + to);
            mailSender.send(message);
            System.out.println("✅ Email sent successfully.");
        } catch (Exception e) {
            System.err.println("❌ Failed to send email: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void sendBlockNotification(String toEmail, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Account Blocked Notification");
        message.setText("Dear " + username + ",\n\nYour account has been blocked by the administrator. You can no longer log in to the system.\n\nRegards,\nSkillSwap Team");

        mailSender.send(message);
    }
    public void sendApprovalNotification(String toEmail, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Account Approved");
        message.setText("Dear " + username + ",\n\nCongratulations! Your account has been approved. You can now log in and access all features of SkillSwap.\n\nBest regards,\nSkillSwap Team");
        mailSender.send(message);
    }

}
