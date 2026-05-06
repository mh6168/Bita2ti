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
import java.util.Random;
import com.bita2ti.bita2ti.model.OrganizationAccessCode;
import com.bita2ti.bita2ti.repository.OrganizationAccessCodeRepository;

@Controller
public class AdminController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserOrganizationRepository userOrgRepo;

    @Autowired
    private OrganizationRepository orgRepo;

    @Autowired
    private OrganizationAccessCodeRepository accessCodeRepo;


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
        Map<Long, String> accessCodes = new HashMap<>();
        for (Organization o : organizations) {
            orgNames.put(o.getId(), o.getName());
            accessCodeRepo.findByOrganizationId(o.getId()).ifPresent(ac -> accessCodes.put(o.getId(), ac.getCode()));
        }
        model.addAttribute("orgNames", orgNames);
        model.addAttribute("accessCodes", accessCodes);

        
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
        Organization savedOrg = orgRepo.save(organization);
        
        // Generate unique access code
        String code;
        do {
            StringBuilder sb = new StringBuilder("ORG-");
            String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            Random random = new Random();
            for (int i = 0; i < 10; i++) {
                sb.append(chars.charAt(random.nextInt(chars.length())));
            }
            code = sb.toString();
        } while (accessCodeRepo.findByCode(code).isPresent());

        OrganizationAccessCode accessCode = new OrganizationAccessCode();
        accessCode.setCode(code);
        accessCode.setOrganization(savedOrg);
        accessCodeRepo.save(accessCode);
        
        return "redirect:/admin/dashboard";
    }


}
