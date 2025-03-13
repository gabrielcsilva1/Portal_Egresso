package com.gabrielcsilva1.Portal_Egresso.exeptions;

import com.gabrielcsilva1.Portal_Egresso.exeptions.core.BadRequestException;

public class GraduateNotFoundException extends BadRequestException {
  public GraduateNotFoundException() { super("Egresso não encontrado"); }
}
