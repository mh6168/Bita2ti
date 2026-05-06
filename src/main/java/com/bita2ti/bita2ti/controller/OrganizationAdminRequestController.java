package com.bita2ti.bita2ti.controller;

import com.bita2ti.bita2ti.model.Organization;
import com.bita2ti.bita2ti.model.OrganizationAdmin;
import com.bita2ti.bita2ti.model.User;
import com.bita2ti.bita2ti.service.OrganizationAdminService;
import com.bita2ti.bita2ti.repository.OrganizationRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/org-admin")
public class OrganizationAdminRequestController {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private OrganizationAdminService organizationAdminService;

    @GetMapping("/request")
    public String requestForm(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        List<Organization> organizations = organizationRepository.findAll();
        model.addAttribute("organizations", organizations);
        model.addAttribute("userId", user.getId());
        return "org-admin-request";
    }

    @PostMapping("/request")
    public String submitRequest(
            @RequestParam Long organizationId,
            HttpSession session,
            Model model
    ) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        // create request if not exists
        organizationAdminService.requestAdmin(user.getId(), organizationId);

        return "redirect:/org-admin/pending";

    }
}

