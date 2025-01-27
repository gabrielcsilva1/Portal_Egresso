package com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions;

import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.core.BadRequestException;

public class GraduateNotFoundException extends BadRequestException {
  public GraduateNotFoundException() { super("Egress not found"); }
}
