package com.gabrielcsilva1.Portal_Egresso.infra.presenters;

import java.util.List;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.course.FetchCourseResponse;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.graduate.FetchGraduateResponse;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.graduate.GetGraduateResponse;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.position.GetPositionResponse;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Graduate;

public class GraduatePresenter {
  public static GetGraduateResponse toGetGraduateResponse(Graduate graduate) {
    List<FetchCourseResponse> courses = graduate.getGraduateCourses()
      .stream()
      .map((graduateCourse) -> {
        return CoursePresenter.toFetchCourseResponse(graduateCourse.getCourse());
      }).toList();
    
    List<GetPositionResponse> positions = graduate.getPositions()
      .stream()
      .map((graduatePosition) -> {
        return PositionPresenter.toGetPositionResponse(graduatePosition);
      }).toList();

    return GetGraduateResponse.builder()
      .id(graduate.getId())
      .courses(courses)
      .positions(positions)
      .name(graduate.getName())
      .email(graduate.getEmail())
      .description(graduate.getDescription())
      .avatarUrl(graduate.getAvatarUrl())
      .linkedin(graduate.getLinkedin())
      .instagram(graduate.getInstagram())
      .curriculum(graduate.getCurriculum())
      .build();
  }

  public static FetchGraduateResponse toFetchGraduateResponse(Graduate graduate) {
    List<String> courses = graduate.getGraduateCourses()
      .stream()
      .map((graduateCourse) -> {
        return graduateCourse.getCourse().getName();
      }).toList();

    List<String> positions = graduate.getPositions()
      .stream()
      .map((graduatePositions) -> {
        return graduatePositions.getDescription();
      }).toList();

    return FetchGraduateResponse.builder()
      .id(graduate.getId())
      .name(graduate.getName())
      .avatarUrl(graduate.getAvatarUrl())
      .courses(courses)
      .positions(positions)
      .build();
  }
}
