package com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions;

public class ResourceNotFoundException extends RuntimeException{
  public ResourceNotFoundException() {
    super("Bad request");
  }

  public ResourceNotFoundException(String message) {
    super(message);
  }
}
