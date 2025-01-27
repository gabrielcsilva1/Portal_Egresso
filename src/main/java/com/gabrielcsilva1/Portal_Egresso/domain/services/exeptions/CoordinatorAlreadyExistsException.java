package com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions;

import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.core.ConflictException;

public class CoordinatorAlreadyExistsException extends ConflictException{
  public CoordinatorAlreadyExistsException() { 
    super("Coordinator already exists"); 
  }
}
