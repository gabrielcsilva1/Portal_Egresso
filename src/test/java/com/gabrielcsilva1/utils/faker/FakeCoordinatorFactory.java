package com.gabrielcsilva1.utils.faker;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Coordinator;
import com.github.javafaker.Faker;

public class FakeCoordinatorFactory {
  static public Coordinator makeCoordinator() {
    var faker = new Faker();
    
    return Coordinator.builder()
      .login(faker.name().username())
      .password(faker.internet().password())
      .build();
  }
}
