package com.pake.pake.Services;

import com.pake.pake.Entities.User;
import com.pake.pake.Repository.FraudActivityRepository;
import com.pake.pake.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class FraudDetectionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private FraudActivityRepository fraudActivityRepository;

    @Transactional
    public boolean blockUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Toggle user status
        boolean isBlocked = "Blocked".equals(user.getUserType());
        String newStatus = isBlocked ? "Active" : "Blocked";
        user.setUserType(newStatus);

        // If blocking the user, invalidate their access
        if (!isBlocked) {
            // Set blocked status
            user.setUserType("Blocked");

            // Send notification email to user
            String message = String.format(
                    "Dear %s,\n\nYour account has been blocked due to suspicious activity. " +
                            "Please contact support for assistance.",
                    user.getUsername());

            emailService.sendEmail(user.getEmail(),
                    "Account Blocked", message);
        }

        userRepository.save(user);
        return !isBlocked; // returns true if user was blocked, false if unblocked
    }

    @Transactional
    public void checkForFraudulentActivity(String userEmail) {
        try {
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // If user is already blocked, don't process further
            if ("Blocked".equals(user.getUserType())) {
                return;
            }

            // Count suspicious activities
            long suspiciousActivities = fraudActivityRepository
                    .countByUserAndTimestampAfter(userEmail,
                            LocalDateTime.now().minusDays(1));

            if (suspiciousActivities >= 3) {
                blockUser(userEmail);
            }
        } catch (Exception e) {
            System.err.println("Error in fraud detection: " + e.getMessage());
        }
    }
}