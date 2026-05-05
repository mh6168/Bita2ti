package com.bita2ti.bita2ti.controller;

import com.bita2ti.bita2ti.model.User;
import com.bita2ti.bita2ti.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // =========================
    // SHOW SIGNUP PAGE
    // =========================
    @GetMapping("/signup")
    public String signupPage(HttpSession session) {

        // If already logged in → redirect to dashboard
        if (session.getAttribute("user") != null) {
            return "redirect:/dashboard";
        }

        return "signup";
    }

    // =========================
    // REGISTER USER
    // =========================
    @PostMapping("/signup")
    public String signup(@ModelAttribute User user,
                         HttpSession session) {

        User savedUser = userService.register(user);

        // store user in session
        session.setAttribute("user", savedUser);

        return "redirect:/dashboard";
    }

    // =========================
    // SHOW LOGIN PAGE
    // =========================
    @GetMapping("/login")
    public String loginPage(HttpSession session) {

        // If already logged in → redirect to dashboard
        if (session.getAttribute("user") != null) {
            return "redirect:/dashboard";
        }

        return "login";
    }

    // =========================
    // LOGIN USER
    // =========================
    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        Optional<User> user = userService.login(email, password);

        if (user.isPresent()) {

            session.setAttribute("user", user.get());

            return "redirect:/dashboard";
        }

        model.addAttribute("error", "Invalid email or password");

        return "login";
    }

    // =========================
    // LOGOUT
    // =========================
    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "redirect:/login";
    }
}