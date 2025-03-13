package com.gabrielcsilva1.Portal_Egresso.dtos.response.jobOpportunity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.JobOpportunity;
import com.gabrielcsilva1.Portal_Egresso.dtos.enums.StatusEnum;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ResponseShortJobOpportunityJson {
  private UUID id;
  private String title;
  private String description;
  private StatusEnum registrationStatus;
  private LocalDateTime createdAt;

  public static ResponseShortJobOpportunityJson toResponse(JobOpportunity jobOpportunity) {

    return ResponseShortJobOpportunityJson.builder()
      .id(jobOpportunity.getId())
      .title(jobOpportunity.getTitle())
      .description(jobOpportunity.getDescription())
      .registrationStatus(jobOpportunity.getRegistrationStatus())
      .createdAt(jobOpportunity.getCreatedAt())
      .build();
  }
}
