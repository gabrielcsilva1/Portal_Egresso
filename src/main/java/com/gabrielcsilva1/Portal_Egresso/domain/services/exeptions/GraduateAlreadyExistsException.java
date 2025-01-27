package com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions;

import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.core.ConflictException;

public class GraduateAlreadyExistsException extends ConflictException{
  public GraduateAlreadyExistsException(String key, String identifier) { 
    super("Graduate with " + key + " '" + identifier + "' already exists"); 
  }
}
