package com.gabrielcsilva1.utils.faker;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Graduate;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Testimonial;
import com.gabrielcsilva1.Portal_Egresso.dtos.enums.StatusEnum;
import com.github.javafaker.Faker;

public class FakeTestimonial {
  static public Testimonial makeTestimonial(Graduate graduate) {
    var faker = new Faker();

    return Testimonial.builder()
      .graduate(graduate)
      .text(faker.lorem().paragraph())
      .registrationStatus(StatusEnum.ACCEPTED)
      .build();
  }
}
