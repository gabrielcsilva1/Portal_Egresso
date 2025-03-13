package com.gabrielcsilva1.Portal_Egresso.domain.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Graduate;
import com.gabrielcsilva1.Portal_Egresso.dtos.enums.StatusEnum;

public interface GraduateRepository extends JpaRepository<Graduate, UUID>, JpaSpecificationExecutor<Graduate>{
  Optional<Graduate> findByEmail(String email);
  Page<Graduate> findByRegistrationStatus(StatusEnum registrationStatus, Pageable pageable);
}
