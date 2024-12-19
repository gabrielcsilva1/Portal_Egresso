package com.gabrielcsilva1.Portal_Egresso.domain.dtos.course;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateCourseDTO {
  @NotBlank
  private String name;
  @NotBlank
  private String level;
}
