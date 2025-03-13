package com.gabrielcsilva1.utils.faker;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Coordinator;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Course;
import com.github.javafaker.Faker;

public class FakeCourseFactory {
  static public Course makeCourse(Coordinator coordinator) {
    var faker = new Faker();

    return Course.builder()
      .coordinator(coordinator)
      .name(faker.educator().course())
      .level(faker.lorem().word())
      .build();
  }
}
