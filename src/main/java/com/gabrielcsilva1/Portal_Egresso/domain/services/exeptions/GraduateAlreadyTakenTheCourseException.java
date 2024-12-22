package com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions;

public class GraduateAlreadyTakenTheCourseException extends RuntimeException{
  public GraduateAlreadyTakenTheCourseException(String courseName) {
    super("Egress already took the course '" + courseName + "'");
  }
}
