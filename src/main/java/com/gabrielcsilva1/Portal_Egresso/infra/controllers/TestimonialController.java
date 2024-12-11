package com.gabrielcsilva1.Portal_Egresso.infra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.TestimonialDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.usecases.CreateEgressTestimonialUseCase;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/testimonial")
public class TestimonialController {
  @Autowired
  private CreateEgressTestimonialUseCase createEgressTestimonialUseCase;

  @PostMapping
  public ResponseEntity<Object> save(@Valid @RequestBody TestimonialDTO testimonialDTO) {
    this.createEgressTestimonialUseCase.execute(testimonialDTO);

    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }
}
