package com.gabrielcsilva1.Portal_Egresso.dtos.response.course;

import java.util.UUID;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Course;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseShortCourseJson {
  private UUID id;
  private String name;
  private String level;

  public static ResponseShortCourseJson toResponse(Course course) {
    return ResponseShortCourseJson.builder()
      .id(course.getId())
      .name(course.getName())
      .level(course.getLevel())
      .build();
  }
}
