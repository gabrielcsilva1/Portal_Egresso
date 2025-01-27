package com.gabrielcsilva1.Portal_Egresso.domain.dtos;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Coordinator;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CoordinatorDTO {
  @NotBlank
  private String login;

  @NotBlank
  private String password;

  public Coordinator toEntity() {
    return Coordinator.builder()
        .login(login)
        .password(password)
        .build();
  }
}
