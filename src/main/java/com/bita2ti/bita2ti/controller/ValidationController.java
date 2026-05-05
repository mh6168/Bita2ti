package com.bita2ti.bita2ti.controller;

import com.bita2ti.bita2ti.repository.UserOrganizationRepository;
import com.bita2ti.bita2ti.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/validate")
public class ValidationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserOrganizationRepository userOrgRepo;

    @GetMapping
    public String validate(@RequestParam String digitalId,
                           @RequestParam Long orgId) {

        return userRepository.findByDigitalId(digitalId)
                .flatMap(user -> userOrgRepo.findByUserIdAndOrganizationId(user.getId(), orgId))
                .map(uo -> "REGISTERED")
                .orElse("NOT REGISTERED");
    }
}