package com.gabrielcsilva1.Portal_Egresso.domain.dtos.testimonial;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetTestimonialResponse {
  private UUID id;
  private UUID graduateId;
  private String text;
  private LocalDateTime createdAt;
}
