package com.gabrielcsilva1.Portal_Egresso.domain.dtos;

import java.util.UUID;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.JobOpportunity;
import com.gabrielcsilva1.Portal_Egresso.infra.validation.ValidUUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobOpportunityDTO {
  @NotNull
  @ValidUUID
  private UUID graduateId;

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
