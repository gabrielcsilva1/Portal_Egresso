package com.gabrielcsilva1.Portal_Egresso.exeptions;

import com.gabrielcsilva1.Portal_Egresso.exeptions.core.ConflictException;

public class GraduateAlreadyExistsException extends ConflictException{
  public GraduateAlreadyExistsException(String key, String identifier) { 
    super("Egresso com " + key + " '" + identifier + "' já existe"); 
  }
}
