package com.gabrielcsilva1.Portal_Egresso.dtos.request.graduateCourse;

import java.util.UUID;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.GraduateCourse;
import com.gabrielcsilva1.Portal_Egresso.infra.validation.ValidUUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class RequestGraduateCourseJson {
  @NotNull
  @ValidUUID
  private UUID courseId;

  @NotNull
  @ValidUUID
  private UUID graduateId;

  @NotNull
  @Positive
  private Integer startYear;

  private Integer endYear;

  public GraduateCourse toEntity() {
    return GraduateCourse.builder()
      .startYear(startYear)
      .endYear(endYear)
      .build();
  }
}
