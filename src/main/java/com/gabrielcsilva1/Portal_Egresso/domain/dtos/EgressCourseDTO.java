package com.gabrielcsilva1.Portal_Egresso.domain.dtos;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EgressCourseDTO {
  @NotNull
  private UUID egressId;

  @NotNull
  private UUID courseId;

  @NotNull
  @Positive
  private Integer startYear;

  @Positive
  private Integer endYear;
}
