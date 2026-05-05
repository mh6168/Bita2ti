package com.bita2ti.bita2ti.controller;

import com.bita2ti.bita2ti.model.User;
import com.bita2ti.bita2ti.service.SubscriptionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    // =========================
    // SUBSCRIBE USER TO ORG
    // =========================
    @PostMapping("/subscribe")
    public String subscribe(@RequestParam Long orgId,
                            HttpSession session) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        subscriptionService.subscribe(user.getId(), orgId);

        return "redirect:/dashboard";
    }
}