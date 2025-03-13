package com.gabrielcsilva1.Portal_Egresso.dtos.request.testimonial;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestCreateTestimonialJson {
  @NotBlank
  private String text;
}
