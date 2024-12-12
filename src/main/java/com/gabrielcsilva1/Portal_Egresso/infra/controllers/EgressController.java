package com.gabrielcsilva1.Portal_Egresso.infra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.EgressDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.PositionDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.TestimonialDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.services.EgressService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/egress")
public class EgressController { 
  @Autowired
  EgressService egressService;

  @PostMapping
  public ResponseEntity<Object> createEgress(@Valid @RequestBody EgressDTO egressDTO) {
    this.egressService.createEgress(egressDTO);

    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }

  @PostMapping
  @RequestMapping("/position")
  public ResponseEntity<Object> registerEgressPosition(@Valid @RequestBody PositionDTO positionDTO) {
    this.egressService.registerEgressPosition(positionDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }

  @PostMapping
  @RequestMapping("/testimonial")
  public ResponseEntity<Object> registerEgressTestimonial(@Valid @RequestBody TestimonialDTO testimonialDTO) {
    this.egressService.registerEgressTestimonial(testimonialDTO);

    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }
}
