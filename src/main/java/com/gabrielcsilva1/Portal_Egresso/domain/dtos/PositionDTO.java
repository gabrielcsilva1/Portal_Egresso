package com.gabrielcsilva1.Portal_Egresso.domain.dtos;

import java.util.UUID;

import com.gabrielcsilva1.Portal_Egresso.infra.validation.ValidUUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PositionDTO {
  @NotNull
  @ValidUUID
  private UUID graduateId;
  
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
