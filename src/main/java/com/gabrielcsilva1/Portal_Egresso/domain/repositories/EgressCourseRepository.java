package com.gabrielcsilva1.Portal_Egresso.domain.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Course;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Egress;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.EgressCourse;

public interface EgressCourseRepository extends JpaRepository<EgressCourse, UUID>{
  Optional<EgressCourse> findByAlumnusAndCourse(Egress egress, Course course);
}
