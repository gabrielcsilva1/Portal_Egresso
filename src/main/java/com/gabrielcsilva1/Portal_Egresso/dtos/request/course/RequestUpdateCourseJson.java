package com.gabrielcsilva1.Portal_Egresso.dtos.request.course;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestUpdateCourseJson {
  @NotBlank
  private String name;
  @NotBlank
  private String level;
}
