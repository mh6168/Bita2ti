package com.bita2ti.bita2ti.controller;

import com.bita2ti.bita2ti.model.Organization;
import com.bita2ti.bita2ti.model.OrganizationAdmin;
import com.bita2ti.bita2ti.model.User;
import com.bita2ti.bita2ti.repository.OrganizationRepository;
import com.bita2ti.bita2ti.repository.UserRepository;
import com.bita2ti.bita2ti.service.OrganizationAdminService;
import com.bita2ti.bita2ti.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class OrganizationAdminAuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private OrganizationAdminService organizationAdminService;

    @Autowired
    private UserRepository userRepository;

    // =========================
    // ORGANIZATION ADMIN SIGNUP
    // =========================

    @GetMapping("/admin/signup")
    public String orgAdminSignupPage(HttpSession session, Model model) {
        if (session.getAttribute("user") != null) {
            return "redirect:/dashboard";
        }

        model.addAttribute("organizations", organizationRepository.findAll());
        return "org-admin-signup";
    }

    @PostMapping("/admin/signup")
    public String orgAdminSignup(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam(required = false, defaultValue = "") String fullName,
            @RequestParam Long organizationId,
            HttpSession session,
            Model model
    ) {
        // 1) register the user (reuses existing UserService hashing + digitalId)
        User tmp = new User();
        tmp.setEmail(email);
        tmp.setPassword(password);
        tmp.setFullName(fullName.isBlank() ? "Organization Admin" : fullName);

        User savedUser = userService.register(tmp);
        session.setAttribute("user", savedUser);

        // 2) request admin for organization
        organizationAdminService.requestAdmin(savedUser.getId(), organizationId);

        // 3) notify
        Optional<OrganizationAdmin> request = organizationAdminService.getRequest(savedUser.getId(), organizationId);
        if (request.isPresent() && Boolean.TRUE.equals(request.get().getApproved())) {
            return "redirect:/org-admin/pending";
        }

        model.addAttribute("message", "Request sent. Waiting for organization admin approval.");
        return "org-admin-pending";
    }

    // =========================
    // ORGANIZATION ADMIN LOGIN
    // =========================

    @GetMapping("/admin/login")
    public String orgAdminLoginPage(HttpSession session) {
        if (session.getAttribute("user") != null) {
            return "redirect:/dashboard";
        }
        return "org-admin-login";
    }

    @PostMapping("/admin/login")
    public String orgAdminLogin(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam Long organizationId,
            HttpSession session,
            Model model
    ) {
        // verify user credentials
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            model.addAttribute("error", "Invalid email or password");
            return "org-admin-login";
        }

        Optional<User> logged = userService.login(email, password);
        if (logged.isEmpty()) {
            model.addAttribute("error", "Invalid email or password");
            return "org-admin-login";
        }

        User u = logged.get();

        // must be approved for that org
        boolean approved = organizationAdminService.isApproved(u.getId(), organizationId);
        if (!approved) {
            model.addAttribute("error", "Your organization admin request is not approved yet.");
            return "org-admin-login";
        }

        session.setAttribute("user", u);
        session.setAttribute("adminOrganizationId", organizationId);

        return "redirect:/org-admin/requests";
    }

    @GetMapping("/org-admin/requests")
    public String orgAdminRequests(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        Long orgId = (Long) session.getAttribute("adminOrganizationId");

        if (user == null || orgId == null) {
            return "redirect:/admin/login";
        }

        // security check
        if (!organizationAdminService.isApproved(user.getId(), orgId)) {
            return "redirect:/admin/login";
        }

        List<OrganizationAdmin> pending = organizationAdminService.getPendingRequests(orgId);
        model.addAttribute("pendingRequests", pending);
        model.addAttribute("organizationId", orgId);

        return "org-admin-requests";
    }

    @PostMapping("/org-admin/requests/approve")
    public String approveRequest(
            @RequestParam Long requestId,
            @RequestParam Long organizationId,
            HttpSession session
    ) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/admin/login";
        }

        if (!organizationAdminService.isApproved(user.getId(), organizationId)) {
            return "redirect:/admin/login";
        }

        organizationAdminService.setApproved(requestId, true);
        return "redirect:/org-admin/requests";
    }

    @PostMapping("/org-admin/requests/reject")
    public String rejectRequest(
            @RequestParam Long requestId,
            @RequestParam Long organizationId,
            HttpSession session
    ) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/admin/login";
        }

        if (!organizationAdminService.isApproved(user.getId(), organizationId)) {
            return "redirect:/admin/login";
        }

        organizationAdminService.setApproved(requestId, false);
        return "redirect:/org-admin/requests";
    }
}

