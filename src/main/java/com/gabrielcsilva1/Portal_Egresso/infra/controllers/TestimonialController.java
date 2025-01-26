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
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.testimonial.TestimonialResponse;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.testimonial.UpdateTestimonialDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Testimonial;
import com.gabrielcsilva1.Portal_Egresso.domain.services.TestimonialService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/graduate/testimonial")
@Tag(name = "Testimonial", description = "Manages the graduate's testimonials")
public class TestimonialController {
  @Autowired
  private TestimonialService testimonialService;

  @PostMapping
  @ApiResponse(responseCode = "201", description = "Register graduate testimonial")
  public ResponseEntity<TestimonialResponse> registerGraduateTestimonial(@Valid @RequestBody TestimonialDTO testimonialDTO) {
    Testimonial testimonial = this.testimonialService.registerGraduateTestimonial(testimonialDTO);

    return ResponseEntity.status(HttpStatus.CREATED).body(
      TestimonialResponse.toResponse(testimonial)
    );
  }

  @GetMapping
  @ApiResponse(responseCode = "201", description = "Register graduate testimonial")
  public ResponseEntity<PaginatedResponse<TestimonialResponse>> fetchGraduateTestimonials(
    @RequestParam(required = false) Integer year, 
    @RequestParam(defaultValue = "1") int page, 
    @RequestParam(defaultValue = "20") int size
    ) {
    Page<Testimonial> testimonialsPaginated = this.testimonialService.fetchTestimonials(year, page-1, size);

    List<TestimonialResponse> testimonialPresenter = testimonialsPaginated.getContent()
      .stream()
      .map(TestimonialResponse::toResponse)
      .toList();

    PaginatedResponse<TestimonialResponse> testimonialPresenterPaginated = new PaginatedResponse<>(
      testimonialPresenter,
      testimonialsPaginated.getNumber(),
      testimonialsPaginated.getTotalPages(),
      testimonialsPaginated.getTotalElements()
    );

    return ResponseEntity.ok(testimonialPresenterPaginated);
  }

  @PutMapping("/{testimonialId}")
  @ApiResponse(responseCode = "204", description = "Graduate testimonial updated")
  public ResponseEntity<Void> updateGraduateTestimonial(
    @PathVariable UUID testimonialId, 
    @Valid @RequestBody UpdateTestimonialDTO testimonialDTO
    ) {
      this.testimonialService.updateGraduateTestimonial(testimonialId, testimonialDTO.getText());
      return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

  @DeleteMapping("{testimonialId}")
  @ApiResponse(responseCode = "204", description = "Graduate testimonial deleted")
  public ResponseEntity<Void> deleteGraduateTestimonial(@PathVariable UUID testimonialId) {
    this.testimonialService.deleteGraduateTestimonial(testimonialId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
  }
}
