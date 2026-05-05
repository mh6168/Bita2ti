package com.bita2ti.bita2ti.controller;

import com.bita2ti.bita2ti.model.User;
import com.bita2ti.bita2ti.service.SubscriptionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("name", user.getFullName());
        model.addAttribute("digitalId", user.getDigitalId());

        model.addAttribute("orgs",
                subscriptionService.getAllOrganizations());

        model.addAttribute("subscriptions",
                subscriptionService.getUserSubscriptions(user.getId()));

        return "dashboard";
    }
}