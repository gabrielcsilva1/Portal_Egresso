package com.gabrielcsilva1.Portal_Egresso.dtos.response.graduate;

import java.util.List;
import java.util.UUID;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Graduate;
import com.gabrielcsilva1.Portal_Egresso.dtos.response.course.ResponseShortCourseJson;
import com.gabrielcsilva1.Portal_Egresso.dtos.response.position.ResponsePositionJson;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseGraduateJson {
  private UUID id;
  private List<ResponseShortCourseJson> courses;
  private List<ResponsePositionJson> positions;
  private String name;
  private String email;
  private String description;
  private String avatarUrl;
  private String linkedin;
  private String instagram;
  private String curriculum;


  public static ResponseGraduateJson toResponse(Graduate graduate) {
    List<ResponseShortCourseJson> courses = graduate.getGraduateCourses()
      .stream()
      .map((graduateCourse) -> {
        return ResponseShortCourseJson.toResponse(graduateCourse.getCourse());
      }).toList();
    
    List<ResponsePositionJson> positions = graduate.getPositions()
      .stream()
      .map((graduatePosition) -> {
        return ResponsePositionJson.toResponse(graduatePosition);
      }).toList();

    return ResponseGraduateJson.builder()
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
