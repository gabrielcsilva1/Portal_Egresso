package com.gabrielcsilva1.Portal_Egresso.dtos.request.course;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Course;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RequestCreateCourseJson {
  @NotBlank
  private String name;

  @NotBlank
  private String level;

  public Course toDomain() {
    return Course.builder()
      .name(name)
      .level(level)
      .build();
  }
}
