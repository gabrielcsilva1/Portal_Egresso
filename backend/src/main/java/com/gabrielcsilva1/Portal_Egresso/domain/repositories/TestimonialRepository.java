package com.gabrielcsilva1.Portal_Egresso.domain.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Testimonial;
import com.gabrielcsilva1.Portal_Egresso.dtos.enums.StatusEnum;

public interface TestimonialRepository extends JpaRepository<Testimonial, UUID>{
  Page<Testimonial> findByRegistrationStatus(StatusEnum registrationStatus, Pageable pageable);

  List<Testimonial> findByGraduateIdOrderByCreatedAtDesc(UUID graduateId);

  @Query("SELECT t FROM Testimonial t WHERE YEAR(t.createdAt) = :year")
  Page<Testimonial> findByYear(@Param("year") int year, Pageable pageable);
}
