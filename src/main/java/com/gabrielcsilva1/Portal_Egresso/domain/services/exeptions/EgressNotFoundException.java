package com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions;

public class EgressNotFoundException extends RuntimeException {
  public EgressNotFoundException() { super("Egress not found"); }
}
