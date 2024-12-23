package com.gabrielcsilva1.Portal_Egresso.domain.dtos.position;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetPositionResponse {
  private UUID id;
  private String description;
  private String location;
  private Integer startYear;
  private Integer endYear;
}
