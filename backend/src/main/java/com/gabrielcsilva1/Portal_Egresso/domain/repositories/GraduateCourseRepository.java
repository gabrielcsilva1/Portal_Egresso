package com.gabrielcsilva1.Portal_Egresso.domain.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Course;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Graduate;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.GraduateCourse;

public interface GraduateCourseRepository extends JpaRepository<GraduateCourse, UUID>{
  Optional<GraduateCourse> findByGraduateAndCourse(Graduate graduate, Course course);
}
