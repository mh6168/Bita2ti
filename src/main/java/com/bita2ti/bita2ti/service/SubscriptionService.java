package com.bita2ti.bita2ti.service;

import com.bita2ti.bita2ti.model.Organization;
import com.bita2ti.bita2ti.model.UserOrganization;
import com.bita2ti.bita2ti.repository.OrganizationRepository;
import com.bita2ti.bita2ti.repository.UserOrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.bita2ti.bita2ti.service.TransactionService;



@Service
public class SubscriptionService {

    @Autowired
    private UserOrganizationRepository userOrgRepo;

    @Autowired
    private OrganizationRepository organizationRepo;

    @Autowired
    private TransactionService transactionService;


    // =========================
    // SUBSCRIBE
    // =========================
    public void subscribe(Long userId, Long orgId) {

        if (userOrgRepo.findByUserIdAndOrganizationId(userId, orgId).isPresent()) {
            return;
        }


        UserOrganization uo = new UserOrganization();
        uo.setUserId(userId);
        uo.setOrganizationId(orgId);
        uo.setStatus(UserOrganization.Status.APPROVED);

        userOrgRepo.save(uo);

        // 🧾 Record transaction
        transactionService.record(userId, "USER_SUBSCRIBED");
    }


    // =========================
    // GET ALL ORGANIZATIONS
    // =========================
    public List<Organization> getAllOrganizations() {
        return organizationRepo.findAll();
    }

    // =========================
    // GET USER SUBSCRIPTIONS
    // =========================
    public List<UserOrganization> getUserSubscriptions(Long userId) {
        return userOrgRepo.findByUserId(userId);
    }

    // =========================
    // MAP orgId -> orgName
    // =========================
    public java.util.Map<Long, String> getOrgNamesById() {
        java.util.Map<Long, String> map = new java.util.HashMap<>();
        for (Organization org : organizationRepo.findAll()) {
            map.put(org.getId(), org.getName());
        }
        return map;
    }
}
