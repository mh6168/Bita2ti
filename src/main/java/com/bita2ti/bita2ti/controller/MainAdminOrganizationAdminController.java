package com.bita2ti.bita2ti.controller;

import com.bita2ti.bita2ti.model.Organization;
import com.bita2ti.bita2ti.model.OrganizationAdmin;
import com.bita2ti.bita2ti.model.User;
import com.bita2ti.bita2ti.repository.OrganizationAdminRepository;
import com.bita2ti.bita2ti.repository.OrganizationRepository;
import com.bita2ti.bita2ti.repository.UserRepository;
import com.bita2ti.bita2ti.service.OrganizationAdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class MainAdminOrganizationAdminController {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private OrganizationAdminService organizationAdminService;

    @Autowired
    private OrganizationAdminRepository organizationAdminRepository;

    @Autowired
    private UserRepository userRepository;

    // NOTE: uses the same hardcoded main admin login logic as AuthController
    private boolean isMainAdmin(HttpSession session) {
        User u = (User) session.getAttribute("user");
        return u != null && "admin@admin.com".equals(u.getEmail());
    }

    @GetMapping("/organization-admin-requests")
    public String orgAdminRequests(@RequestParam(required = false) Long organizationId,
                                      HttpSession session,
                                      Model model) {

        if (!isMainAdmin(session)) {
            return "redirect:/login";
        }

        List<Organization> organizations = organizationRepository.findAll();
        model.addAttribute("organizations", organizations);

        if (organizationId == null) {
            model.addAttribute("selectedOrganizationId", null);
            model.addAttribute("pending", List.of());
            return "admin-org-admin-requests";
        }

        model.addAttribute("selectedOrganizationId", organizationId);

        List<OrganizationAdmin> pending = organizationAdminService.getPendingRequests(organizationId);
        model.addAttribute("pending", pending);

        // For displaying user email/fullName
        Map<Long, String> userLabelById = new HashMap<>();
        List<Long> userIds = pending.stream().map(OrganizationAdmin::getUserId).distinct().collect(Collectors.toList());
        for (Long userId : userIds) {
            userRepository.findById(userId).ifPresent(user ->
                    userLabelById.put(userId, user.getFullName() + " (" + user.getEmail() + ")")
            );
        }
        model.addAttribute("userLabelById", userLabelById);

        return "admin-org-admin-requests";
    }

    @PostMapping("/organization-admin-requests/approve")
    public String approve(@RequestParam Long requestId,
                            @RequestParam Long organizationId,
                            HttpSession session) {

        if (!isMainAdmin(session)) {
            return "redirect:/login";
        }

        // Approve first, then notify the user
        organizationAdminService.setApproved(requestId, true);

        // Notify approved org-admin by email
        organizationAdminService.notifyApprovedRequest(requestId);


        return "redirect:/admin/organization-admin-requests?organizationId=" + organizationId;
    }

    @PostMapping("/organization-admin-requests/reject")
    public String reject(@RequestParam Long requestId,
                           @RequestParam Long organizationId,
                           HttpSession session) {

        if (!isMainAdmin(session)) {
            return "redirect:/login";
        }

        organizationAdminService.setApproved(requestId, false);
        return "redirect:/admin/organization-admin-requests?organizationId=" + organizationId;
    }
}

