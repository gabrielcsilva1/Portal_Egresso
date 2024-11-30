package com.gabrielcsilva1.Portal_Egresso.domain.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Egress;

public interface EgressRepository extends JpaRepository<Egress, UUID>{
  Optional<Egress> findByEmail(String email);
}
