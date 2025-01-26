package com.gabrielcsilva1.Portal_Egresso.domain.dtos.jobOpportunity;

import java.util.UUID;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.JobOpportunity;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class JobOpportunityResponse {
  private UUID id;
  private String title;
  private String description;
  private Boolean isVerified;

  public static JobOpportunityResponse toResponse(JobOpportunity jobOpportunity) {
    return JobOpportunityResponse.builder()
      .id(jobOpportunity.getId())
      .title(jobOpportunity.getTitle())
      .description(jobOpportunity.getDescription())
      .isVerified(jobOpportunity.getIsVerified())
      .build();
  }
}
