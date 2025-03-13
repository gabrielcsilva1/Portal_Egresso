package com.gabrielcsilva1.utils.faker;

import java.time.LocalDateTime;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Graduate;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Position;
import com.github.javafaker.Faker;

public class FakePositionFactory {
  static public Position makePosition(Graduate graduate) {
    var faker = new Faker();
    
    return Position.builder()
      .graduate(graduate)
      .description(faker.lorem().paragraph())
      .location(faker.company().name())
      .startYear(faker.number().numberBetween(0, LocalDateTime.now().getYear()))
      .build();
  }
}
