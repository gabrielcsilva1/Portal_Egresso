package com.gabrielcsilva1.Portal_Egresso.infra.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Graduate;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Testimonial;
import com.gabrielcsilva1.Portal_Egresso.domain.services.TestimonialService;
import com.gabrielcsilva1.Portal_Egresso.dtos.paginated.ResponsePaginated;
import com.gabrielcsilva1.Portal_Egresso.dtos.request.RequestUpdateRegisterStatusJson;
import com.gabrielcsilva1.Portal_Egresso.dtos.request.testimonial.RequestCreateTestimonialJson;
import com.gabrielcsilva1.Portal_Egresso.dtos.request.testimonial.RequestUpdateTestimonialJson;
import com.gabrielcsilva1.Portal_Egresso.dtos.response.testimonial.ResponseShortTestimonialJson;
import com.gabrielcsilva1.Portal_Egresso.dtos.response.testimonial.ResponseTestimonialJson;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/testimonial")
@Tag(name = "Testimonial", description = "Manages the graduate's testimonials")
public class TestimonialController {
  @Autowired
  private TestimonialService testimonialService;

  @PostMapping
  @PreAuthorize("hasRole('GRADUATE')")
  @ApiResponse(responseCode = "201", description = "Register graduate testimonial")
  public ResponseEntity<ResponseTestimonialJson> registerGraduateTestimonial(@Valid @RequestBody RequestCreateTestimonialJson testimonialDTO) {
    var authentication = SecurityContextHolder.getContext().getAuthentication();

    var graduate = (Graduate) authentication.getPrincipal(); 

    Testimonial testimonial = this.testimonialService.registerGraduateTestimonial(graduate.getId(), testimonialDTO);

    return ResponseEntity.status(HttpStatus.CREATED).body(
      ResponseTestimonialJson.toResponse(testimonial)
    );
  }

  @GetMapping
  @ApiResponse(responseCode = "201", description = "Register graduate testimonial")
  public ResponseEntity<ResponsePaginated<ResponseTestimonialJson>> fetchTestimonials(
    @RequestParam(required = false) Integer year, 
    @RequestParam(defaultValue = "0") int pageIndex
    ) {
    Page<Testimonial> testimonialsPaginated = this.testimonialService.fetchTestimonials(year, pageIndex);

    List<ResponseTestimonialJson> testimonialPresenter = testimonialsPaginated.getContent()
      .stream()
      .map(ResponseTestimonialJson::toResponse)
      .toList();

    ResponsePaginated<ResponseTestimonialJson> testimonialPresenterPaginated = new ResponsePaginated<>(
      testimonialPresenter,
      testimonialsPaginated.getNumber(),
      testimonialsPaginated.getTotalPages(),
      testimonialsPaginated.getTotalElements()
    );

    return ResponseEntity.ok(testimonialPresenterPaginated);
  }

  @GetMapping("/unverified")
  @ApiResponse(responseCode = "201", description = "Register graduate testimonial")
  public ResponseEntity<ResponsePaginated<ResponseTestimonialJson>> fetchUnverifiedGraduateTestimonials(
    @RequestParam(defaultValue = "0") int pageIndex
    ) {
    Page<Testimonial> pageTestimonials = this.testimonialService.fetchUnverifiedTestimonials(pageIndex);

    List<ResponseTestimonialJson> testimonialsJson = pageTestimonials.getContent()
      .stream()
      .map(ResponseTestimonialJson::toResponse)
      .toList();

    ResponsePaginated<ResponseTestimonialJson> responseJson = new ResponsePaginated<>(
      testimonialsJson,
      pageTestimonials.getNumber(),
      pageTestimonials.getTotalPages(),
      pageTestimonials.getTotalElements()
    );

    return ResponseEntity.ok(responseJson);
  }

  @PutMapping("/{testimonialId}")
  @ApiResponse(responseCode = "204", description = "Graduate testimonial updated")
  public ResponseEntity<Void> updateGraduateTestimonial(
    @PathVariable UUID testimonialId, 
    @Valid @RequestBody RequestUpdateTestimonialJson testimonialDTO
    ) {
      this.testimonialService.updateGraduateTestimonial(testimonialId, testimonialDTO.getText());
      return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

  @PutMapping("/{testimonialId}/status")
  @PreAuthorize("hasRole('COORDINATOR')")
  @ApiResponse(responseCode = "204", description = "Graduate testimonial updated")
  public ResponseEntity<Void> updateGraduateTestimonial(
    @PathVariable UUID testimonialId, 
    @Valid @RequestBody RequestUpdateRegisterStatusJson requestJson
    ) {
      this.testimonialService.updateTestimonialRegisterStatus(testimonialId, requestJson.getNewStatus());
      return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

  @DeleteMapping("{testimonialId}")
  @ApiResponse(responseCode = "204", description = "Graduate testimonial deleted")
  public ResponseEntity<Void> deleteGraduateTestimonial(@PathVariable UUID testimonialId) {
    this.testimonialService.deleteGraduateTestimonial(testimonialId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
  }

  @GetMapping("/graduate/{graduateId}")
  @ApiResponse(responseCode = "200")
  public ResponseEntity<List<ResponseShortTestimonialJson>> fetchGraduateTestimonials(
    @PathVariable UUID graduateId
    ) {
    List<Testimonial> testimonialsList = this.testimonialService.fetchGraduateTestimonials(graduateId);

    List<ResponseShortTestimonialJson> responseJson = testimonialsList
      .stream()
      .map(ResponseShortTestimonialJson::toResponse)
      .toList();

    return ResponseEntity.ok(responseJson);
  }
}
