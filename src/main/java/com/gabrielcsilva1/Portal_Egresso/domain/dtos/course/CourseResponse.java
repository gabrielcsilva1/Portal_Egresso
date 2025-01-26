package com.gabrielcsilva1.Portal_Egresso.domain.dtos.course;

import java.util.UUID;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Course;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CourseResponse {
  private UUID id;
  private String name;
  private String level;

  public static CourseResponse toResponse(Course course) {
    return CourseResponse.builder()
      .id(course.getId())
      .name(course.getName())
      .level(course.getLevel())
      .build();
  }
}
