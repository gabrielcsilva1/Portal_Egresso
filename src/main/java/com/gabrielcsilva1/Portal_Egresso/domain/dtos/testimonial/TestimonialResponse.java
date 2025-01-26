package com.gabrielcsilva1.Portal_Egresso.domain.dtos.testimonial;

import java.time.LocalDateTime;
import java.util.UUID;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Testimonial;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TestimonialResponse {
  private UUID id;
  private UUID graduateId;
  private String text;
  private LocalDateTime createdAt;

  public static TestimonialResponse toResponse(Testimonial testimonial) {
    return TestimonialResponse.builder()
      .id(testimonial.getId())
      .graduateId(testimonial.getGraduate().getId())
      .text(testimonial.getText())
      .createdAt(testimonial.getCreatedAt())
      .build();
  }
}
