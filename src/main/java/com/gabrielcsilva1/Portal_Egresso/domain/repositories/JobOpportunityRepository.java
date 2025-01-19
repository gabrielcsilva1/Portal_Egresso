package com.gabrielcsilva1.Portal_Egresso.domain.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.JobOpportunity;

public interface JobOpportunityRepository extends JpaRepository<JobOpportunity, UUID>{
  List<JobOpportunity> findByIsVerifiedTrue();
  List<JobOpportunity> findByIsVerifiedFalse();
}
