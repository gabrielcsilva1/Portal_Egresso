package com.gabrielcsilva1.Portal_Egresso.infra.presenters;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.position.GetPositionResponse;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Position;

public class PositionPresenter {
  public static GetPositionResponse toGetPositionResponse(Position position) {
    return GetPositionResponse.builder()
      .id(position.getId())
      .description(position.getDescription())
      .location(position.getLocation())
      .startYear(position.getStartYear())
      .endYear(position.getEndYear())
      .build();
  }
}
