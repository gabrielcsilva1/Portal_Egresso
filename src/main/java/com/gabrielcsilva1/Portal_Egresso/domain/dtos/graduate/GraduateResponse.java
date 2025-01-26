package com.gabrielcsilva1.Portal_Egresso.domain.dtos.graduate;

import java.util.List;
import java.util.UUID;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.course.CourseResponse;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.position.PositionResponse;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Graduate;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GraduateResponse {
  private UUID id;
  private List<CourseResponse> courses;
  private List<PositionResponse> positions;
  private String name;
  private String email;
  private String description;
  private String avatarUrl;
  private String linkedin;
  private String instagram;
  private String curriculum;


  public static GraduateResponse toResponse(Graduate graduate) {
    List<CourseResponse> courses = graduate.getGraduateCourses()
      .stream()
      .map((graduateCourse) -> {
        return CourseResponse.toResponse(graduateCourse.getCourse());
      }).toList();
    
    List<PositionResponse> positions = graduate.getPositions()
      .stream()
      .map((graduatePosition) -> {
        return PositionResponse.toResponse(graduatePosition);
      }).toList();

    return GraduateResponse.builder()
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
}
