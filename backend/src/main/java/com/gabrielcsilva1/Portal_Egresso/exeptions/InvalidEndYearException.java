package com.gabrielcsilva1.Portal_Egresso.exeptions;

public class InvalidEndYearException extends RuntimeException {
  public InvalidEndYearException() {
    super("Ano de término inválido. O ano de término precisa ser maior que o ano de início.");
  }
}
