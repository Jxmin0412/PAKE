package com.pake.pake.Controllers;

import com.pake.pake.Entities.User;
import com.pake.pake.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @RequestParam("username") String username,
            @RequestParam("password1") String password,
            @RequestParam("gen") String gender,
            @RequestParam("dt") String dateOfBirth,
            @RequestParam("mobile") String mobile,
            @RequestParam("email") String email,
            @RequestParam("filename") String filename,
            @RequestParam("file") MultipartFile file) {

        try {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password); // In production, password should be encrypted
            user.setGender(gender);
            user.setDateOfBirth(dateOfBirth);
            user.setMobile(mobile);
            user.setEmail(email);
            user.setFilename(filename);

            boolean success = userService.registerUser(user, file);

            if (success) {
                return ResponseEntity.ok()
                        .body("{\"message\": \"User registered successfully\", \"redirectUrl\": \"./user.html\"}");
            } else {
                return ResponseEntity.badRequest().body("{\"message\": \"Username or email already exists\"}");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("{\"message\": \"Registration failed: " + e.getMessage() + "\"}");
        }
    }
}