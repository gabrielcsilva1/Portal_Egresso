package com.gabrielcsilva1.Portal_Egresso.domain.dtos;

import java.util.UUID;

import com.gabrielcsilva1.Portal_Egresso.infra.validation.ValidUUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EgressCourseDTO {
  @NotNull
  @ValidUUID
  private UUID egressId;

  @NotNull
  @ValidUUID
  private UUID courseId;

  @NotNull
  @Positive
  private Integer startYear;

  @Positive
  private Integer endYear;
}
