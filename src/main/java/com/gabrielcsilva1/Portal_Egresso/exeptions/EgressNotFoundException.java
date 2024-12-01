package com.gabrielcsilva1.Portal_Egresso.exeptions;

public class EgressNotFoundException extends RuntimeException {
  public EgressNotFoundException() { super("Egress not found"); }
}
