package com.gabrielcsilva1.Portal_Egresso.dtos.request.jobOpportunity;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestUpdateJobOpportunityJson {
  @NotBlank
  private String title;

  @NotBlank
  private String description;
}
