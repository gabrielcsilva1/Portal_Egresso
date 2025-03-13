package com.gabrielcsilva1.Portal_Egresso.dtos.response.graduate;

import java.util.List;
import java.util.UUID;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Graduate;
import com.gabrielcsilva1.Portal_Egresso.dtos.enums.StatusEnum;
import com.gabrielcsilva1.Portal_Egresso.dtos.response.graduateCourse.ResponseGraduateCourseJson;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseShortGraduateJson {
  private UUID id;
  private String name;
  private String avatarUrl;
  private List<ResponseGraduateCourseJson> courses;
  private StatusEnum registrationStatus;

  public static ResponseShortGraduateJson toResponse(Graduate graduate) {
    var courses = graduate.getGraduateCourses().stream().map(ResponseGraduateCourseJson::toResponse).toList();

    return ResponseShortGraduateJson.builder()
      .id(graduate.getId())
      .name(graduate.getName())
      .avatarUrl(graduate.getAvatarUrl())
      .courses(courses)
      .registrationStatus(graduate.getRegistrationStatus())
      .build();
  }
}
