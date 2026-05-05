package com.bita2ti.bita2ti.repository;

import com.bita2ti.bita2ti.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
}