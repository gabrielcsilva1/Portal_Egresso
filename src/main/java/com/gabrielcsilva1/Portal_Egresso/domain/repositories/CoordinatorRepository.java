package com.gabrielcsilva1.Portal_Egresso.domain.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Coordinator;

public interface CoordinatorRepository extends JpaRepository<Coordinator, UUID> {
  
}
