package com.gabrielcsilva1.Portal_Egresso.domain.dtos.course;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FetchCourseResponse {
  private UUID id;
  private String name;
  private String level;
}
