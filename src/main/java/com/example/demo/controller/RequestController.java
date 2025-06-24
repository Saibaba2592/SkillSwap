package com.example.demo.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.RequestDTO;
import com.example.demo.entity.Request;
import com.example.demo.entity.User;
import com.example.demo.service.EmailService;
import com.example.demo.service.RequestService;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/api/request")
public class RequestController {

    @Autowired
    private RequestService requestService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    @PreAuthorize("hasRole('ROLE_USER')")
    public Request sendRequest(Principal principal, @RequestBody RequestDTO dto) {
        User sender = userService.findByUsername(principal.getName()).orElseThrow();
        User receiver = userService.findByUsername(dto.getReceiverUsername()).orElseThrow();
        return requestService.sendRequest(sender, receiver);
    }

    @PostMapping("/respond")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> respondByUsername(
            Principal principal,
            @RequestParam String toUsername,
            @RequestParam String status,
            @RequestParam(required = false) String meetLink
    ) {
        User receiver = userService.findByUsername(principal.getName()).orElseThrow();
        User sender = userService.findByUsername(toUsername).orElseThrow();

        List<Request> requests = requestService.requestRepository.findBySender(sender)
            .stream()
            .filter(req -> req.getReceiver().getId().equals(receiver.getId()))
            .toList();

        if (requests.isEmpty()) {
            return ResponseEntity.badRequest().body("No matching request found.");
        }

        Request req = requests.get(0); // assume latest or first

        Request updated = requestService.respondRequest(req, status);

        if ("ACCEPTED".equalsIgnoreCase(status) && meetLink != null) {
            emailService.sendRequestAcceptedEmail(
                sender.getEmail(),
                receiver.getUsername(),
                sender.getUsername(),
                meetLink
            );
        }

        if ("COMPLETED".equalsIgnoreCase(status)) {
            System.out.println("✅ COMPLETED status block triggered.");
            System.out.println("📧 Feedback request email should be triggered here.");
        }

        return ResponseEntity.ok("Request status updated successfully.");
    }


    @GetMapping("/sent")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<Request> getSentRequests(Principal principal) {
        User sender = userService.findByUsername(principal.getName()).orElseThrow();
        return requestService.requestRepository.findBySender(sender);
    }
    @GetMapping("/incoming")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<RequestDTO> getIncomingRequests(Principal principal) {
        User receiver = userService.findByUsername(principal.getName()).orElseThrow();
        List<Request> requests = requestService.requestRepository.findByReceiver(receiver);

        return requests.stream().map(r -> new RequestDTO(
            r.getId(),
            r.getSender().getUsername(),
            r.getReceiver().getUsername(),
            r.getStatus(),
            r.getTimestamp()
        )).toList();
    }

}
