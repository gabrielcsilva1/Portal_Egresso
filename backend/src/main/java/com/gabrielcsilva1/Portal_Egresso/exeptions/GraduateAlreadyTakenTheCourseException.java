package com.gabrielcsilva1.Portal_Egresso.exeptions;

public class GraduateAlreadyTakenTheCourseException extends RuntimeException{
  public GraduateAlreadyTakenTheCourseException() {
    super("Egresso já foi cadastrado nesse curso");
  }
}
