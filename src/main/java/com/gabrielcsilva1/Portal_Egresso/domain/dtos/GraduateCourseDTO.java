package com.gabrielcsilva1.Portal_Egresso.domain.dtos;

import java.util.UUID;

import com.gabrielcsilva1.Portal_Egresso.infra.validation.ValidUUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class GraduateCourseDTO {
  @NotNull
  @ValidUUID
  private UUID graduateId;

  @NotNull
  @ValidUUID
  private UUID courseId;

  @NotNull
  @Positive
  private Integer startYear;

  @Positive
  private Integer endYear;
}
