package com.gabrielcsilva1.Portal_Egresso.domain.dtos.jobOpportunity;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateJobOpportunityDTO {
  @NotBlank
  private String title;

  @NotBlank
  private String description;
}
