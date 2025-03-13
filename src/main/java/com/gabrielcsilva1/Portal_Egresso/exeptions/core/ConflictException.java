package com.gabrielcsilva1.Portal_Egresso.exeptions.core;

public abstract class ConflictException extends RuntimeException{
  public ConflictException() {
    super("Conflict");
  }

  public ConflictException(String message) {
    super(message);
  }
}
