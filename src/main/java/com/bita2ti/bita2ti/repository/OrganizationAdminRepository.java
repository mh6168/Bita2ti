package com.bita2ti.bita2ti.repository;

import com.bita2ti.bita2ti.model.OrganizationAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrganizationAdminRepository extends JpaRepository<OrganizationAdmin, Long> {

    List<OrganizationAdmin> findByOrganizationIdAndApproved(Long organizationId, Boolean approved);

    Optional<OrganizationAdmin> findByUserIdAndOrganizationId(Long userId, Long organizationId);

    List<OrganizationAdmin> findByApproved(Boolean approved);
}

