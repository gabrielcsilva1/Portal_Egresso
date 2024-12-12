package com.gabrielcsilva1.Portal_Egresso.infra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.TestimonialDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.services.TestimonialService;

import jakarta.validation.Valid;

@RestController
public class TestimonialController {
  @Autowired
  private TestimonialService testimonialService;

  @PostMapping
  @RequestMapping("/egress/testimonial")
  public ResponseEntity<Object> registerEgressTestimonial(@Valid @RequestBody TestimonialDTO testimonialDTO) {
    this.testimonialService.registerEgressTestimonial(testimonialDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }
}
