package com.gabrielcsilva1.Portal_Egresso.infra.presenters;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.testimonial.GetTestimonialResponse;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Testimonial;

public class TestimonialPresenter {
  public static GetTestimonialResponse toGetTestimonialResponse(Testimonial testimonial) {
    return GetTestimonialResponse.builder()
      .id(testimonial.getId())
      .graduateId(testimonial.getGraduate().getId())
      .text(testimonial.getText())
      .createdAt(testimonial.getCreatedAt())
      .build();
  }
}
