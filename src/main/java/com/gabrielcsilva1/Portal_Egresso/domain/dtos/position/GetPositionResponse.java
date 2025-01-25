package com.gabrielcsilva1.Portal_Egresso.domain.dtos.position;

import java.util.UUID;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Position;

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

  public static GetPositionResponse toResponse(Position position) {
    return GetPositionResponse.builder()
      .id(position.getId())
      .description(position.getDescription())
      .location(position.getLocation())
      .startYear(position.getStartYear())
      .endYear(position.getEndYear())
      .build();
  }
}
