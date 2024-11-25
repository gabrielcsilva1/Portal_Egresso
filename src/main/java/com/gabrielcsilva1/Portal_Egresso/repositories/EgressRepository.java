package com.gabrielcsilva1.Portal_Egresso.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabrielcsilva1.Portal_Egresso.entities.Egress;

public interface EgressRepository extends JpaRepository<Egress, UUID>{
}
