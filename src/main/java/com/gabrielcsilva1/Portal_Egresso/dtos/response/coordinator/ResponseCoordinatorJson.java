package com.gabrielcsilva1.Portal_Egresso.dtos.response.coordinator;

import java.util.UUID;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Coordinator;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ResponseCoordinatorJson {
  private UUID id;
  private String login;

  static public ResponseCoordinatorJson toResponse(Coordinator coordinator) {
    return ResponseCoordinatorJson.builder()
      .id(coordinator.getId())
      .login(coordinator.getLogin())
      .build();
  }
}
