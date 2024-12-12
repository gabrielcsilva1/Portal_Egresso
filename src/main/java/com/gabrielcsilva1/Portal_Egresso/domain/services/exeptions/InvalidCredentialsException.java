package com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions;

public class InvalidCredentialsException extends RuntimeException{
  public InvalidCredentialsException() {
    super("Invalid credentials. Please check your login and password.");
  }
}
