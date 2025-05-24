package com.pake.pake.Controllers;

import com.pake.pake.DTO.LoginRequest;
import com.pake.pake.Entities.User;
import com.pake.pake.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        try {
            // First check if user is blocked
            User user = userService.findByUsername(loginRequest.getUsername());

            if (user != null && "Blocked".equals(user.getUserType())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                        "success", false,
                        "message", "Your account has been blocked. Please contact support."));
            }

            // Proceed with normal login logic
            user = userService.validateLogin(
                    loginRequest.getUsername(),
                    loginRequest.getPassword(),
                    loginRequest.getSecretKey(),
                    loginRequest.getFileData(),
                    loginRequest.getFileName());

            if (user != null) {
                // Store user information in session
                session.setAttribute("userId", user.getId());
                session.setAttribute("username", user.getUsername());
                session.setAttribute("userType", user.getUserType());

                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Login successful",
                        "redirectUrl", "./user_card.html"));
            } else {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "Invalid credentials. Please try again."));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Login failed: " + e.getMessage()));
        }
    }
}