package com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.core;

public abstract class BadRequestException extends RuntimeException{
  public BadRequestException() {
    super("Bad request");
  }

  public BadRequestException(String message) {
    super(message);
  }
}
