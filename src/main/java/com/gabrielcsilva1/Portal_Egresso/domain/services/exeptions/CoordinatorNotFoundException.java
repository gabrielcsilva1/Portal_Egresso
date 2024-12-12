package com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions;

public class CoordinatorNotFoundException extends RuntimeException{
  public CoordinatorNotFoundException() {
    super("Coordinator not found.");
  }
}
