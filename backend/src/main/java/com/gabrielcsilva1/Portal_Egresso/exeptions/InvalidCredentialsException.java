package com.gabrielcsilva1.Portal_Egresso.exeptions;

public class InvalidCredentialsException extends RuntimeException{
  public InvalidCredentialsException() {
    super("Credencias inválidas. Por favor verifique seu login e senha.");
  }
}
