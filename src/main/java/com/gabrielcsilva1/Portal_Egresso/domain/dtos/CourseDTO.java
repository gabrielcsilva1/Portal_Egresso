package com.gabrielcsilva1.Portal_Egresso.domain.dtos;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseDTO {

  @NotBlank
  private UUID coordinatorId;

  @NotBlank
  private String name;

  @NotBlank
  private String level;
}
