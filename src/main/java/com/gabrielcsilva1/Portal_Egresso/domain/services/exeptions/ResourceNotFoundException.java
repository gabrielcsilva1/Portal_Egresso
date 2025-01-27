package com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions;

import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.core.BadRequestException;

public class ResourceNotFoundException extends BadRequestException{
  public ResourceNotFoundException() {
    super();
  }

  public ResourceNotFoundException(String message) {
    super(message);
  }
}
