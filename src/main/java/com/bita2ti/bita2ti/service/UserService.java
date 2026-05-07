package com.bita2ti.bita2ti.service;

import com.bita2ti.bita2ti.model.User;
import com.bita2ti.bita2ti.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private EmailService emailService; // ✅ NEW

    @Autowired
    private TransactionService transactionService;


    @Autowired
    private PasswordEncoder passwordEncoder;

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

        // ✅ Hash password
        user.setPassword(passwordEncoder.encode(user.getPassword()));


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

        // 🧾 Record transaction
        transactionService.record(savedUser.getId(), "ACCOUNT_CREATED");

        return savedUser;
    }

    public Optional<User> login(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return user;
        }

        return Optional.empty();
    }
}
