package com.gabrielcsilva1.Portal_Egresso.dtos.request.jobOpportunity;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.JobOpportunity;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestCreateJobOpportunityJson {
  @NotBlank
  private String title;

  @NotBlank
  private String description;

  public JobOpportunity toJobOpportunity() {
    return JobOpportunity.builder()
      .title(this.title)
      .description(this.description)
      .build();
  }
}
