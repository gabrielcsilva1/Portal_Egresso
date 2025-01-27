package com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions;

import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.core.BadRequestException;

public class CourseNotFoundException extends BadRequestException {
  public CourseNotFoundException() {
    super("Course not found.");
  }
}
