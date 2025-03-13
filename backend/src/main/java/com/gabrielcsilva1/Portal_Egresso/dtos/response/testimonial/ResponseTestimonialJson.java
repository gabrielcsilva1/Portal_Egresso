package com.gabrielcsilva1.Portal_Egresso.dtos.response.testimonial;

import java.time.LocalDateTime;
import java.util.UUID;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Testimonial;
import com.gabrielcsilva1.Portal_Egresso.dtos.enums.StatusEnum;
import com.gabrielcsilva1.Portal_Egresso.dtos.response.graduate.ResponseShortGraduateJson;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseTestimonialJson {
  private UUID id;
  private ResponseShortGraduateJson graduate;
  private String text;
  private LocalDateTime createdAt;
  private StatusEnum registrationStatus;

  public static ResponseTestimonialJson toResponse(Testimonial testimonial) {
    var graduate = ResponseShortGraduateJson.toResponse(testimonial.getGraduate());

    return ResponseTestimonialJson.builder()
      .id(testimonial.getId())
      .graduate(graduate)
      .text(testimonial.getText())
      .createdAt(testimonial.getCreatedAt())
      .registrationStatus(testimonial.getRegistrationStatus())
      .build();
  }
}
