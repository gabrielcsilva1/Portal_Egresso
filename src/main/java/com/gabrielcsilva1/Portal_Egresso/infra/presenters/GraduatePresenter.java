package com.gabrielcsilva1.Portal_Egresso.infra.presenters;

import java.util.List;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.graduate.FetchGraduateResponse;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Graduate;

public class GraduatePresenter {
  public static FetchGraduateResponse toFetchGraduateResponse(Graduate graduate) {
    List<String> courses = graduate.getGraduateCourse()
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
