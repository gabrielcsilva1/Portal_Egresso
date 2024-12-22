package com.gabrielcsilva1.Portal_Egresso.infra.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.TestimonialDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.services.TestimonialService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/egress/testimonial")
@Tag(name = "Testimonial", description = "Manages the graduate's testimonials")
public class TestimonialController {
  @Autowired
  private TestimonialService testimonialService;

  @PostMapping
  public ResponseEntity<Object> registerEgressTestimonial(@Valid @RequestBody TestimonialDTO testimonialDTO) {
    this.testimonialService.registerEgressTestimonial(testimonialDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }

  @GetMapping
  public ResponseEntity<Object> fetchEgressTestimonials(
    @RequestParam Integer year, 
    @RequestParam(defaultValue = "1") int page, 
    @RequestParam(defaultValue = "20") int size
    ) {
    return ResponseEntity.ok(this.testimonialService.fetchTestimonials(year, page, size));
  }

  @PutMapping("/{testimonialId}")
  public ResponseEntity<Object> updateEgressTestimonial(
    @PathVariable UUID testimonialId, 
    @Valid @RequestBody String text
    ) {
      this.testimonialService.updateEgressTestimonial(testimonialId, text);
      return ResponseEntity.status(HttpStatus.OK).body(null);
    }

  @DeleteMapping("{testimonialId}")
  public ResponseEntity<Object> deleteEgressTestimonial(@PathVariable UUID testimonialId) {
    this.testimonialService.deleteEgressTestimonial(testimonialId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
  }
}
