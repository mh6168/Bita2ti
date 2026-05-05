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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserOrganizationRepository userOrgRepo;

    @Autowired
    private OrganizationRepository orgRepo;

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
    public String validationForm(@RequestParam(required = false) Long subscriptionId, Model model) {
        if (subscriptionId == null) {
            model.addAttribute("serviceName", null);
        } else {
            Optional<Organization> orgOpt = orgRepo.findById(subscriptionId);
            model.addAttribute("serviceName", orgOpt.map(Organization::getName).orElse("Unknown"));
            model.addAttribute("subscriptionId", subscriptionId);
        }
        return "validation-form";
    }

    @GetMapping("/lookup")
    public String lookupPage(@RequestParam String digitalId,
                             @RequestParam Long subscriptionId,
                             Model model) {

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
