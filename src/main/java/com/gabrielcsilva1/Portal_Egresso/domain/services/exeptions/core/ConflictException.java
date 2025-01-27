package com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.core;

public abstract class ConflictException extends RuntimeException{
  public ConflictException() {
    super("Conflict");
  }

  public ConflictException(String message) {
    super(message);
  }
}
