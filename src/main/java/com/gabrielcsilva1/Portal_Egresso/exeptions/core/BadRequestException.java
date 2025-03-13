package com.gabrielcsilva1.Portal_Egresso.exeptions.core;

public class BadRequestException extends RuntimeException{
  public BadRequestException() {
    super("Bad request");
  }

  public BadRequestException(String message) {
    super(message);
  }
}
