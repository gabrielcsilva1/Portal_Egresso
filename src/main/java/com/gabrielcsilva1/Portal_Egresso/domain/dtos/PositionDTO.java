package com.gabrielcsilva1.Portal_Egresso.domain.dtos;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PositionDTO {
  private UUID egressoId;
  
  @NotBlank
  private String description;

  @NotBlank
  private String company;

  @Positive
  @NotNull
  private Integer startYear;

  @Positive
  private Integer endYear;
}
