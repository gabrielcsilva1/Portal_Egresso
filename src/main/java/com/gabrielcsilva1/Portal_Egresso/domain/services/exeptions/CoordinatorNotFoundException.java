package com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions;

import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.core.BadRequestException;

public class CoordinatorNotFoundException extends BadRequestException{
  public CoordinatorNotFoundException() {
    super("Coordinator not found.");
  }
}
