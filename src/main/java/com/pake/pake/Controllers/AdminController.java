package com.pake.pake.Controllers;

import com.pake.pake.DTO.UserDTO;
import com.pake.pake.Entities.User;
import com.pake.pake.Repository.UserRepository;
import com.pake.pake.Services.FraudDetectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private FraudDetectionService fraudDetectionService;

        @GetMapping("/users")
        public List<UserDTO> getAllUsers() {
                // First check for fraud for all users
                userRepository.findAll()
                                .forEach(user -> fraudDetectionService.checkForFraudulentActivity(user.getEmail()));

                // Then return the updated user list
                return userRepository.findAll().stream()
                                .map(user -> new UserDTO(
                                                user.getUsername(),
                                                user.getGender(),
                                                user.getDateOfBirth(),
                                                user.getMobile(),
                                                user.getEmail(),
                                                user.getUserType()))
                                .collect(Collectors.toList());
        }

        @PostMapping("/users/{email}/block")
        public ResponseEntity<?> blockUser(@PathVariable String email) {
                try {
                        // Call fraud detection service to block user
                        boolean blocked = fraudDetectionService.blockUser(email);

                        if (blocked) {
                                return ResponseEntity.ok(Map.of(
                                                "success", true,
                                                "message", "User blocked successfully",
                                                "newStatus", "Blocked"));
                        } else {
                                return ResponseEntity.ok(Map.of(
                                                "success", true,
                                                "message", "User unblocked successfully",
                                                "newStatus", "Active"));
                        }
                } catch (Exception e) {
                        return ResponseEntity.badRequest().body(Map.of(
                                        "success", false,
                                        "message", "Failed to update user status: " + e.getMessage()));
                }
        }
}