package com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions;

public class EgressAlreadyTakenTheCourseException extends RuntimeException{
  public EgressAlreadyTakenTheCourseException(String courseName) {
    super("Egress already took the course '" + courseName + "'");
  }
}
