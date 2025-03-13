package com.gabrielcsilva1.Portal_Egresso.dtos.request;

import com.gabrielcsilva1.Portal_Egresso.dtos.enums.StatusEnum;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RequestUpdateRegisterStatusJson {
  @NotNull
  private StatusEnum newStatus;
}
