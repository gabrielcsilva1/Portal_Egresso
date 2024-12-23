package com.gabrielcsilva1.Portal_Egresso.infra.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.paginated.PaginatedResponse;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.testimonial.GetTestimonialResponse;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Testimonial;
import com.gabrielcsilva1.Portal_Egresso.domain.services.TestimonialService;
import com.gabrielcsilva1.Portal_Egresso.infra.presenters.TestimonialPresenter;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/graduate/testimonial")
@Tag(name = "Testimonial", description = "Manages the graduate's testimonials")
public class TestimonialController {
  @Autowired
  private TestimonialService testimonialService;

  @PostMapping
  public ResponseEntity<Object> registerGraduateTestimonial(@Valid @RequestBody TestimonialDTO testimonialDTO) {
    this.testimonialService.registerGraduateTestimonial(testimonialDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }

  @GetMapping
  public ResponseEntity<Object> fetchGraduateTestimonials(
    @RequestParam(required = false) Integer year, 
    @RequestParam(defaultValue = "1") int page, 
    @RequestParam(defaultValue = "20") int size
    ) {
    Page<Testimonial> testimonialsPaginated = this.testimonialService.fetchTestimonials(year, page-1, size);

    List<GetTestimonialResponse> testimonialPresenter = testimonialsPaginated.getContent()
      .stream()
      .map(TestimonialPresenter::toGetTestimonialResponse)
      .toList();

    PaginatedResponse<GetTestimonialResponse> testimonialPresenterPaginated = new PaginatedResponse<>(
      testimonialPresenter,
      testimonialsPaginated.getNumber(),
      testimonialsPaginated.getTotalPages(),
      testimonialsPaginated.getTotalElements()
    );

    return ResponseEntity.ok(testimonialPresenterPaginated);
  }

  @PutMapping("/{testimonialId}")
  public ResponseEntity<Object> updateGraduateTestimonial(
    @PathVariable UUID testimonialId, 
    @Valid @RequestBody String text
    ) {
      this.testimonialService.updateGraduateTestimonial(testimonialId, text);
      return ResponseEntity.status(HttpStatus.OK).body(null);
    }

  @DeleteMapping("{testimonialId}")
  public ResponseEntity<Object> deleteGraduateTestimonial(@PathVariable UUID testimonialId) {
    this.testimonialService.deleteGraduateTestimonial(testimonialId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
  }
}
