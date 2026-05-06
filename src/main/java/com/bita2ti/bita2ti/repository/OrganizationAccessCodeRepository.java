package com.bita2ti.bita2ti.repository;

import com.bita2ti.bita2ti.model.OrganizationAccessCode;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OrganizationAccessCodeRepository extends JpaRepository<OrganizationAccessCode, Long> {
    Optional<OrganizationAccessCode> findByCode(String code);
    Optional<OrganizationAccessCode> findByOrganizationId(Long organizationId);
}




