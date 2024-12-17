package com.gabrielcsilva1.Portal_Egresso.infra.presenters;

import java.util.List;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.egress.EgressFilterResponse;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Egress;

public class EgressPresenter {
  public static EgressFilterResponse toEgressFilterResponse(Egress egress) {
    List<String> courses = egress.getEgressCourse()
      .stream()
      .map((egressCourse) -> {
        return egressCourse.getCourse().getName();
      }).toList();

    return EgressFilterResponse.builder()
      .id(egress.getId())
      .name(egress.getName())
      .avatarUrl(egress.getAvatarUrl())
      .courses(courses)
      .build();
  }
}
