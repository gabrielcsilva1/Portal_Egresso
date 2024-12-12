package com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions;

public class CourseNotFoundException extends RuntimeException {
  public CourseNotFoundException() {
    super("Course not found.");
  }
}
