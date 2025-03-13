package com.gabrielcsilva1.Portal_Egresso.dtos.response.jobOpportunity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.JobOpportunity;
import com.gabrielcsilva1.Portal_Egresso.dtos.enums.StatusEnum;
import com.gabrielcsilva1.Portal_Egresso.dtos.response.graduate.ResponseShortGraduateJson;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ResponseJobOpportunityJson {
  private UUID id;
  private String title;
  private String description;
  private ResponseShortGraduateJson graduate;
  private StatusEnum registrationStatus;
  private LocalDateTime createdAt;

  public static ResponseJobOpportunityJson toResponse(JobOpportunity jobOpportunity) {
    ResponseShortGraduateJson graduate = ResponseShortGraduateJson.toResponse(jobOpportunity.getGraduate());

    return ResponseJobOpportunityJson.builder()
      .id(jobOpportunity.getId())
      .title(jobOpportunity.getTitle())
      .description(jobOpportunity.getDescription())
      .graduate(graduate)
      .registrationStatus(jobOpportunity.getRegistrationStatus())
      .createdAt(jobOpportunity.getCreatedAt())
      .build();
  }
}
