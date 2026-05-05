package com.bita2ti.bita2ti.service;

import com.bita2ti.bita2ti.model.User;
import com.bita2ti.bita2ti.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService; // ✅ NEW

    // 🔥 Generate Digital ID
    private String generateDigitalId() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder id = new StringBuilder("EGY-");
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            id.append(chars.charAt(random.nextInt(chars.length())));
        }

        return id.toString();
    }

    public User register(User user) {

        // ✅ Generate unique ID
        String digitalId;
        do {
            digitalId = generateDigitalId();
        } while (userRepository.findByDigitalId(digitalId).isPresent());

        user.setDigitalId(digitalId);

        // 💾 Save user first
        User savedUser = userRepository.save(user);

        // 📩 Send email AFTER saving
        emailService.sendWelcomeEmail(
                savedUser.getEmail(),
                savedUser.getFullName() ,
                savedUser.getDigitalId()
        );

        return savedUser;
    }

    public Optional<User> login(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return user;
        }

        return Optional.empty();
    }
}