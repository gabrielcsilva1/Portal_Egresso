package com.gabrielcsilva1.Portal_Egresso.dtos.response.position;

import java.util.UUID;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Position;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponsePositionJson {
  private UUID id;
  private String description;
  private String location;
  private Integer startYear;
  private Integer endYear;

  public static ResponsePositionJson toResponse(Position position) {
    return ResponsePositionJson.builder()
      .id(position.getId())
      .description(position.getDescription())
      .location(position.getLocation())
      .startYear(position.getStartYear())
      .endYear(position.getEndYear())
      .build();
  }
}
