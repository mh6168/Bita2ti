package com.bita2ti.bita2ti.service;

import com.bita2ti.bita2ti.model.OrganizationAdmin;
import com.bita2ti.bita2ti.model.Organization;
import com.bita2ti.bita2ti.model.User;
import com.bita2ti.bita2ti.repository.OrganizationAdminRepository;
import com.bita2ti.bita2ti.repository.OrganizationRepository;
import com.bita2ti.bita2ti.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class OrganizationAdminService {

    @Autowired
    private OrgAdminApprovalEmailService orgAdminApprovalEmailService;


    @Autowired
    private OrganizationAdminRepository organizationAdminRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionService transactionService;


    public void requestAdmin(Long userId, Long organizationId) {
        Optional<OrganizationAdmin> existing = organizationAdminRepository.findByUserIdAndOrganizationId(userId, organizationId);

        if (existing.isPresent()) {
            return;
        }

        OrganizationAdmin admin = new OrganizationAdmin();
        admin.setUserId(userId);
        admin.setOrganizationId(organizationId);
        admin.setApproved(false);
        organizationAdminRepository.save(admin);

        // 🧾 Record transaction
        transactionService.record(userId, "ORG_ADMIN_REQUESTED");
    }


    public List<OrganizationAdmin> getPendingRequests(Long organizationId) {
        return organizationAdminRepository.findByOrganizationIdAndApproved(organizationId, false);
    }

    public List<Organization> getAllApprovedOrganizations() {
        return organizationRepository.findAll();
    }

    public void setApproved(Long requestId, boolean approved) {

        OrganizationAdmin request = organizationAdminRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Admin request not found: " + requestId));

        request.setApproved(approved);
        organizationAdminRepository.save(request);

        // 🧾 Record transaction (approved/rejected)
        transactionService.record(request.getUserId(), approved ? "ORG_ADMIN_APPROVED" : "ORG_ADMIN_REJECTED");
    }


    public boolean isApproved(Long userId, Long organizationId) {
        return organizationAdminRepository
                .findByUserIdAndOrganizationId(userId, organizationId)
                .map(OrganizationAdmin::getApproved)
                .orElse(false);
    }

    public Optional<OrganizationAdmin> getRequest(Long userId, Long organizationId) {
        return organizationAdminRepository.findByUserIdAndOrganizationId(userId, organizationId);
    }

    public List<OrganizationAdmin> findByApproved(Boolean approved) {
        return organizationAdminRepository.findByApproved(approved);
    }

    // Helpers for controller templates
    public Optional<User> findUser(Long userId) {
        return userRepository.findById(userId);
    }

public Optional<Organization> findOrganization(Long organizationId) {
        return organizationRepository.findById(organizationId);
    }

    public void notifyApprovedRequest(Long requestId) {
        organizationAdminRepository.findById(requestId).ifPresent(req -> {
            Long userId = req.getUserId();
            Long orgId = req.getOrganizationId();

            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                return;
            }

            User u = userOpt.get();
            String toEmail = u.getEmail();
            String name = u.getFullName();

            // Link to org-admin validation form
            String validationLink = "/validate/form?subscriptionId=" + orgId;

            orgAdminApprovalEmailService.sendOrgAdminApprovedEmail(toEmail, name, validationLink);
        });
    }
}


