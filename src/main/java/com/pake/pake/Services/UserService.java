package com.pake.pake.Services;

import com.pake.pake.Entities.User;
import com.pake.pake.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.SecretKey;
import java.io.IOException;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EncryptionService encryptionService;

    @Autowired
    private EmailService emailService;

    public boolean registerUser(User user, MultipartFile file) throws Exception {
        if (userRepository.existsByUsername(user.getUsername()) ||
                userRepository.existsByEmail(user.getEmail())) {
            return false;
        }

        // Generate and set secret key
        SecretKey secretKey = encryptionService.generateSecretKey();
        String encodedKey = encryptionService.encodeKey(secretKey);
        String cipherText = encryptionService.encrypt(user.getUsername(), secretKey);

        user.setSecretKey(encodedKey);
        user.setCipherText(cipherText);
        user.setUserType("Normal");

        if (file != null && !file.isEmpty()) {
            user.setFileData(file.getBytes());
        }

        // Save user
        userRepository.save(user);

        // Send email with secret key
        emailService.sendSecretKey(user.getEmail(), encodedKey);

        return true;
    }
    public User validateLogin(String username, String password, String secretKey) {
        User user = userRepository.findByUsername(username);
        if (user != null &&
                user.getPassword().equals(password) &&
                user.getSecretKey().equals(secretKey)) {
            return user;
        }
        return null;
    }
}