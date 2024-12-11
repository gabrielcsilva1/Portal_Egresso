package com.gabrielcsilva1.Portal_Egresso.domain.dtos;

import java.util.UUID;

import com.gabrielcsilva1.Portal_Egresso.infra.validation.ValidUUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseDTO {

  @NotNull
  @ValidUUID
  private UUID coordinatorId;

  @NotBlank
  private String name;

  @NotBlank
  private String level;
}
