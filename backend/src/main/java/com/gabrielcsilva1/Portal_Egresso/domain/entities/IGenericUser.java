package com.gabrielcsilva1.Portal_Egresso.domain.entities;

import java.util.List;
import java.util.UUID;

import com.gabrielcsilva1.Portal_Egresso.dtos.enums.RoleEnum;

public interface IGenericUser {
  public List<RoleEnum> getRoles();
  public UUID getId();
}
