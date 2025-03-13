package com.gabrielcsilva1.Portal_Egresso.domain.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.JobOpportunity;
import com.gabrielcsilva1.Portal_Egresso.dtos.enums.StatusEnum;

public interface JobOpportunityRepository extends JpaRepository<JobOpportunity, UUID>{
  Page<JobOpportunity> findByRegistrationStatus(StatusEnum registrationStatus, Pageable pageable);
  List<JobOpportunity> findByGraduateIdOrderByCreatedAtDesc(UUID graduateId);
}
