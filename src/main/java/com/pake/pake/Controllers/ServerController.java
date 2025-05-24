package com.pake.pake.Controllers;

import com.pake.pake.Entities.User;
import com.pake.pake.Repository.UserRepository;
import com.pake.pake.Services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/server")
public class ServerController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @GetMapping("/users")
    public List<Map<String, String>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> {
            Map<String, String> userMap = new HashMap<>();
            userMap.put("username", user.getUsername());
            userMap.put("email", user.getEmail());
            userMap.put("secretKey", user.getSecretKey());
            return userMap;
        }).collect(Collectors.toList());
    }

    @PostMapping("/sendKey")
    public ResponseEntity<?> sendKeyToUser(@RequestParam String email, @RequestParam String key) {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String message = String.format(
                    "Dear %s,\n\nYour secret key is: %s\n\nBest regards,\nPAKE System",
                    user.getUsername(), key);

            emailService.sendEmail(email, "Your PAKE Secret Key", message);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Secret key sent successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Failed to send key: " + e.getMessage()
            ));
        }
    }
}
