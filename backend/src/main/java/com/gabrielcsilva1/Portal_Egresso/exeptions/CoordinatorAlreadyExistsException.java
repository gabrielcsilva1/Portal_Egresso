package com.gabrielcsilva1.Portal_Egresso.exeptions;

import com.gabrielcsilva1.Portal_Egresso.exeptions.core.ConflictException;

public class CoordinatorAlreadyExistsException extends ConflictException{
  public CoordinatorAlreadyExistsException() { 
    super("Coordenador já existe."); 
  }
}
