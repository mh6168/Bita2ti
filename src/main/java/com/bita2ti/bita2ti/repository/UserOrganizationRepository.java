package com.bita2ti.bita2ti.repository;

import com.bita2ti.bita2ti.model.UserOrganization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserOrganizationRepository extends JpaRepository<UserOrganization, Long> {

    // Get all subscriptions of a user
    List<UserOrganization> findByUserId(Long userId);

    // Check if user already subscribed to an organization
    Optional<UserOrganization> findByUserIdAndOrganizationId(Long userId, Long organizationId);
}