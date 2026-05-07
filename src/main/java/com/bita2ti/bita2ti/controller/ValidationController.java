package com.bita2ti.bita2ti.controller;

import com.bita2ti.bita2ti.model.Organization;
import com.bita2ti.bita2ti.model.User;
import com.bita2ti.bita2ti.model.UserOrganization;
import com.bita2ti.bita2ti.repository.OrganizationRepository;
import com.bita2ti.bita2ti.repository.UserOrganizationRepository;
import com.bita2ti.bita2ti.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/validate")
public class ValidationController {

    private boolean canAccessOrganization(User sessionUser, Long orgId) {
        // Global admin can access all forms.
        // AuthController sets session user digitalId = "ADMIN-0000".
        if (sessionUser != null && "ADMIN-0000".equals(sessionUser.getDigitalId())) {
            return true;
        }

        // Regular subscription access
        if (sessionUser != null && userOrgRepo.findByUserIdAndOrganizationId(sessionUser.getId(), orgId).isPresent()) {
            return true;
        }

        // Organization admin access (approved)
        if (sessionUser != null && organizationAdminService.isApproved(sessionUser.getId(), orgId)) {
            return true;
        }

        return false;
    }





    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserOrganizationRepository userOrgRepo;

    @Autowired
    private OrganizationRepository orgRepo;

    @Autowired
    private com.bita2ti.bita2ti.repository.OrganizationAdminRepository organizationAdminRepository;

    @Autowired
    private com.bita2ti.bita2ti.service.OrganizationAdminService organizationAdminService;


    // Existing REST API
    @GetMapping
    @ResponseBody
    public String validate(@RequestParam String digitalId,
                           @RequestParam Long orgId) {

        return userRepository.findByDigitalId(digitalId)
                .flatMap(user -> userOrgRepo.findByUserIdAndOrganizationId(user.getId(), orgId))
                .map(uo -> "REGISTERED")
                .orElse("NOT REGISTERED");
    }

    // New HTML lookup page
    @GetMapping("/form")
    public String validationForm(
            @RequestParam(required = false) Long subscriptionId,
            Model model,
            jakarta.servlet.http.HttpSession session
    ) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            return "redirect:/login";
        }

        if (subscriptionId == null) {
            model.addAttribute("serviceName", null);
            return "validation-form";
        }

        boolean canAccess = canAccessOrganization(sessionUser, subscriptionId);

        // canAccessOrganization already contains admin/user access logic
        if (!canAccess) {

            // Keep the UI, but ensure the user does not validate that subscription.
            // (validation UI will show Access denied based on serviceName)
            model.addAttribute("serviceName", "Access denied");
            model.addAttribute("subscriptionId", subscriptionId);
            return "validation-form";
        }





        Optional<Organization> orgOpt = orgRepo.findById(subscriptionId);
        model.addAttribute("serviceName", orgOpt.map(Organization::getName).orElse("Unknown"));
        model.addAttribute("subscriptionId", subscriptionId);
        return "validation-form";
    }

    @GetMapping("/lookup")
    public String lookupPage(
            @RequestParam String digitalId,
            @RequestParam Long subscriptionId,
            Model model,
            jakarta.servlet.http.HttpSession session
    ) {

        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            return "redirect:/login";
        }

        if (!canAccessOrganization(sessionUser, subscriptionId)) {

            model.addAttribute("serviceName", "Access denied");
            model.addAttribute("subscribed", false);
            return "validation-result";
        }

        Optional<User> userOpt = userRepository.findByDigitalId(digitalId);
        Optional<Organization> orgOpt = orgRepo.findById(subscriptionId);


        if (userOpt.isEmpty()) {
            model.addAttribute("message", "User not found");
            model.addAttribute("serviceName", orgOpt.map(Organization::getName).orElse("Unknown Service"));
            model.addAttribute("subscribed", false);
            return "validation-result";
        }

        User user = userOpt.get();
        Optional<UserOrganization> subOpt = userOrgRepo.findByUserIdAndOrganizationId(user.getId(), subscriptionId);

        String serviceName = orgOpt.map(Organization::getName).orElse("Unknown Service");
        model.addAttribute("serviceName", serviceName);
        model.addAttribute("user", user);
        model.addAttribute("subscribed", subOpt.isPresent());

        return "validation-result";
    }
}
