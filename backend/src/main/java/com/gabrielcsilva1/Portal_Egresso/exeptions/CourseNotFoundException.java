package com.gabrielcsilva1.Portal_Egresso.exeptions;

import com.gabrielcsilva1.Portal_Egresso.exeptions.core.BadRequestException;

public class CourseNotFoundException extends BadRequestException {
  public CourseNotFoundException() {
    super("Curso não encontrado");
  }
}
