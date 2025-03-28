package com.gabrielcsilva1.Portal_Egresso.domain.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Position;

public interface PositionRepository extends JpaRepository<Position, UUID>{
  List<Position> findByGraduateId(UUID graduateId);
}
