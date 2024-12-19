package com.gabrielcsilva1.Portal_Egresso.domain.dtos.position;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdatePositionDTO {
  @NotBlank
  private String description;

  @NotBlank
  private String location;

  @Positive
  @NotNull
  private Integer startYear;

  @Positive
  private Integer endYear;
}
