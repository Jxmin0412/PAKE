package com.pake.pake.Controllers;

import com.pake.pake.DTO.LoginRequest;
import com.pake.pake.Entities.User;
import com.pake.pake.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
        Map<String, Object> response = new HashMap<>();

        try {
            User user = userService.validateLogin(loginRequest.getUsername(), loginRequest.getPassword(), loginRequest.getSecretKey());

            if (user != null) {
                // Store user information in session
                session.setAttribute("userId", user.getId());
                session.setAttribute("username", user.getUsername());

                response.put("success", true);
                response.put("message", "Login successful");
                response.put("redirectUrl", "./user_card.html");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Invalid username or password or secret key");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Login failed: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}