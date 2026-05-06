package com.bita2ti.bita2ti.controller;

import com.bita2ti.bita2ti.model.User;
import com.bita2ti.bita2ti.service.OrganizationAdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@Controller
@RequestMapping("/org-admin")
public class OrgAdminPendingController {

    @Autowired
    private OrganizationAdminService organizationAdminService;

    @GetMapping("/pending")
    public String pending(
            HttpSession session,
            @RequestParam(required = false) Long organizationId,
            Model model
    ) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        // In your current flow, you can reach this page right after submitting a request.
        // org-admin-pending.html can display generic info; we optionally pass orgId.
        if (organizationId != null) {
            model.addAttribute("organizationId", organizationId);
        }

        // If your template expects some message, add a safe default.
        model.addAttribute("message",
                "Request submitted. Wait for approval by the main admin.");

        // Optional: if you want to show per-org pending state, you can uncomment the next line
        // boolean isPending = organizationAdminService.isPending(user.getId(), organizationId);

        return "org-admin-pending";
    }
}

