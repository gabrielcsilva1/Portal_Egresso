package com.gabrielcsilva1.Portal_Egresso.domain.dtos.graduate;

import java.util.List;
import java.util.UUID;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.course.FetchCourseResponse;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.position.GetPositionResponse;

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
}
