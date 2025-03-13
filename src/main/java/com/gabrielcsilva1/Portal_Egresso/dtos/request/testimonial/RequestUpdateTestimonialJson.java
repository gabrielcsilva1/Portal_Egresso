package com.gabrielcsilva1.Portal_Egresso.dtos.request.testimonial;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RequestUpdateTestimonialJson {
  @NotBlank
  private String text;
}
