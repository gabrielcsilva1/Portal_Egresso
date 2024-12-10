package com.gabrielcsilva1.Portal_Egresso.domain.usecases.exeptions;

public class InvalidEndYearException extends RuntimeException {
  public InvalidEndYearException() {
    super("Invalid end year. The end year must be greater than the start year and cannot be greater than the current year.");
  }
}
