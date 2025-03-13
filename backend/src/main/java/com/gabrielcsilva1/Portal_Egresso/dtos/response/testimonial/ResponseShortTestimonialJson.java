package com.gabrielcsilva1.Portal_Egresso.dtos.response.testimonial;

import java.time.LocalDateTime;
import java.util.UUID;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Testimonial;
import com.gabrielcsilva1.Portal_Egresso.dtos.enums.StatusEnum;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ResponseShortTestimonialJson {
  private UUID id;
  private String text;
  private LocalDateTime createdAt;
  private StatusEnum registrationStatus;

  public static ResponseShortTestimonialJson toResponse(Testimonial testimonial) {

    return ResponseShortTestimonialJson.builder()
      .id(testimonial.getId())
      .text(testimonial.getText())
      .createdAt(testimonial.getCreatedAt())
      .registrationStatus(testimonial.getRegistrationStatus())
      .build();
  }
}
