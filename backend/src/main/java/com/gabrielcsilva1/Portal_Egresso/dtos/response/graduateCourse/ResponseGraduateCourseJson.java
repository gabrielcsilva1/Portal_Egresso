package com.gabrielcsilva1.Portal_Egresso.dtos.response.graduateCourse;

import java.util.UUID;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.GraduateCourse;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ResponseGraduateCourseJson {
  private UUID registerId;
  private UUID courseId;
  private String name;
  private String level;
  private Integer startYear;
  private Integer endYear;

  static public ResponseGraduateCourseJson toResponse(GraduateCourse graduateCourse) {
    return ResponseGraduateCourseJson.builder()
      .registerId(graduateCourse.getId())
      .courseId(graduateCourse.getCourse().getId())
      .name(graduateCourse.getCourse().getName())
      .level(graduateCourse.getCourse().getLevel())
      .startYear(graduateCourse.getStartYear())
      .endYear(graduateCourse.getEndYear())
      .build();
  }
}
