package com.gabrielcsilva1.Portal_Egresso.domain.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Egress;

public interface EgressRepository extends JpaRepository<Egress, UUID>, JpaSpecificationExecutor<Egress>{
  Optional<Egress> findByEmail(String email);
}
