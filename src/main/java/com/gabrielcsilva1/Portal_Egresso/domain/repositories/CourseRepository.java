package com.gabrielcsilva1.Portal_Egresso.domain.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Course;

public interface CourseRepository extends JpaRepository<Course, UUID> {
  public List<Course> findAllByOrderByNameAsc();
}
