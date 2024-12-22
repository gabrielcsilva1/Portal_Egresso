package com.gabrielcsilva1.Portal_Egresso.domain.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Graduate;

public interface GraduateRepository extends JpaRepository<Graduate, UUID>, JpaSpecificationExecutor<Graduate>{
  Optional<Graduate> findByEmail(String email);
}
