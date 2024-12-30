package com.gabrielcsilva1.Portal_Egresso.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CourseDTO {
  @NotBlank
  private String name;

  @NotBlank
  private String level;
}
