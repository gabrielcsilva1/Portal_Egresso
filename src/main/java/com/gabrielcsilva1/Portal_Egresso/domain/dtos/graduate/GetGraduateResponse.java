package com.gabrielcsilva1.Portal_Egresso.domain.dtos.graduate;

import java.util.List;
import java.util.UUID;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.course.FetchCourseResponse;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.position.GetPositionResponse;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Graduate;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetGraduateResponse {
  private UUID id;
  private List<FetchCourseResponse> courses;
  private List<GetPositionResponse> positions;
  private String name;
  private String email;
  private String description;
  private String avatarUrl;
  private String linkedin;
  private String instagram;
  private String curriculum;


  public static GetGraduateResponse toResponse(Graduate graduate) {
    List<FetchCourseResponse> courses = graduate.getGraduateCourses()
      .stream()
      .map((graduateCourse) -> {
        return FetchCourseResponse.toResponse(graduateCourse.getCourse());
      }).toList();
    
    List<GetPositionResponse> positions = graduate.getPositions()
      .stream()
      .map((graduatePosition) -> {
        return GetPositionResponse.toResponse(graduatePosition);
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
}
