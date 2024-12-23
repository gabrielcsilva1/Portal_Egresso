package com.gabrielcsilva1.Portal_Egresso.infra.presenters;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.course.FetchCourseResponse;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Course;

public class CoursePresenter {
  static public FetchCourseResponse toFetchCourseResponse(Course course) {
    return FetchCourseResponse.builder()
      .id(course.getId())
      .name(course.getName())
      .level(course.getLevel())
      .build();
  }
}
