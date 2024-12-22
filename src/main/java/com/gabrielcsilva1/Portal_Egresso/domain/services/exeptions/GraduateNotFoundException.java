package com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions;

public class GraduateNotFoundException extends RuntimeException {
  public GraduateNotFoundException() { super("Egress not found"); }
}
