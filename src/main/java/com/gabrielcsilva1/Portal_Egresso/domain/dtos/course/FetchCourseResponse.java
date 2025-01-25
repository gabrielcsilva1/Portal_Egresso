package com.gabrielcsilva1.Portal_Egresso.domain.dtos.course;

import java.util.UUID;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Course;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FetchCourseResponse {
  private UUID id;
  private String name;
  private String level;

  public static FetchCourseResponse toResponse(Course course) {
    return FetchCourseResponse.builder()
      .id(course.getId())
      .name(course.getName())
      .level(course.getLevel())
      .build();
  }
}
