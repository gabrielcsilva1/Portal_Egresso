package com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions;

public class GraduateAlreadyExistsException extends RuntimeException{
  public GraduateAlreadyExistsException(String key, String identifier) { 
    super("Graduate with " + key + " '" + identifier + "' already exists"); 
  }
}
