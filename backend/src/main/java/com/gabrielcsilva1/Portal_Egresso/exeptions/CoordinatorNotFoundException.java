package com.gabrielcsilva1.Portal_Egresso.exeptions;

import com.gabrielcsilva1.Portal_Egresso.exeptions.core.BadRequestException;

public class CoordinatorNotFoundException extends BadRequestException{
  public CoordinatorNotFoundException() {
    super("Coordenador não encontrado");
  }
}
