package com.gabrielcsilva1.Portal_Egresso.domain.dtos.testimonial;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateTestimonialDTO {
  @NotBlank
  private String text;
}
