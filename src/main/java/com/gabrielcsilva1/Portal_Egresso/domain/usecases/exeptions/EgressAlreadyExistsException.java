package com.gabrielcsilva1.Portal_Egresso.domain.usecases.exeptions;

public class EgressAlreadyExistsException extends RuntimeException{
  public EgressAlreadyExistsException(String key, String identifier) { 
    super("Egress with " + key + " '" + identifier + "' already exists"); 
  }
}