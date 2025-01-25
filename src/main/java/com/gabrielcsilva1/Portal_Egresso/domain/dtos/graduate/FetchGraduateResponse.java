package com.gabrielcsilva1.Portal_Egresso.domain.dtos.graduate;

import java.util.List;
import java.util.UUID;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Graduate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FetchGraduateResponse {
  private UUID id;
  private List<String> courses;
  private List<String> positions;
  private String name;
  private String avatarUrl;

  public static FetchGraduateResponse toResponse(Graduate graduate) {
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
