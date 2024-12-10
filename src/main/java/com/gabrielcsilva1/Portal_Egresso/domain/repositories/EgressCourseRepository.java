package com.gabrielcsilva1.Portal_Egresso.domain.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.EgressCourse;

public interface EgressCourseRepository extends JpaRepository<EgressCourse, UUID>{
  
}
