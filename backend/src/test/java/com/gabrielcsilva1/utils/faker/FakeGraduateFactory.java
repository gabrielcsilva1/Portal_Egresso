package com.gabrielcsilva1.utils.faker;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Graduate;
import com.gabrielcsilva1.Portal_Egresso.dtos.enums.StatusEnum;
import com.github.javafaker.Faker;

public class FakeGraduateFactory {
  static public Graduate makeGraduate() {
    var faker = new Faker();

    return Graduate.builder()
        .name(faker.name().name())
        .email(faker.internet().emailAddress())
        .password(faker.internet().password())
        .registrationStatus(StatusEnum.ACCEPTED)
        .build();
  }
}
