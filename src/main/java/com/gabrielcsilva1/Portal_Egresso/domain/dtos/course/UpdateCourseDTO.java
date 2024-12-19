package com.gabrielcsilva1.Portal_Egresso.domain.dtos.course;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateCourseDTO {
  private String name;
  private String level;
}
