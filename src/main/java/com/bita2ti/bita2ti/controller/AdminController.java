package com.bita2ti.bita2ti.controller;

import com.bita2ti.bita2ti.model.User;
import com.bita2ti.bita2ti.model.UserOrganization;
import com.bita2ti.bita2ti.model.Organization;
import com.bita2ti.bita2ti.repository.UserOrganizationRepository;
import com.bita2ti.bita2ti.repository.UserRepository;
import com.bita2ti.bita2ti.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AdminController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserOrganizationRepository userOrgRepo;

    @Autowired
    private OrganizationRepository orgRepo;

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {

        List<User> users = userRepo.findAll();

        Map<Long, List<UserOrganization>> userSubscriptions = new HashMap<>();

        for (User user : users) {
            userSubscriptions.put(
                    user.getId(),
                    userOrgRepo.findByUserId(user.getId())
            );
        }

        model.addAttribute("users", users);
        model.addAttribute("subscriptions", userSubscriptions);
        List<Organization> organizations = orgRepo.findAll();
        model.addAttribute("organizations", organizations);
        
        Map<Long, String> orgNames = new HashMap<>();
        for (Organization o : organizations) {
            orgNames.put(o.getId(), o.getName());
        }
        model.addAttribute("orgNames", orgNames);
        
        return "admin-dashboard";

    }


    @GetMapping("/admin/add-organization")
    public String addOrganizationForm(Model model) {
        model.addAttribute("organization", new Organization());
        return "admin-add-organization";
    }

    @PostMapping("/admin/add-organization")
    public String addOrganization(@ModelAttribute Organization organization) {
        organization.setApproved(true);
        orgRepo.save(organization);
        return "redirect:/admin/dashboard";
    }
}
