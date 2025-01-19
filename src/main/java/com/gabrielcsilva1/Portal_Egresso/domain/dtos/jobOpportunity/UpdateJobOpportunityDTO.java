package com.gabrielcsilva1.Portal_Egresso.domain.dtos.jobOpportunity;

import java.util.UUID;

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
public class UpdateJobOpportunityDTO {
  @NotNull
  @ValidUUID
  private UUID jobOpportunityId;

  @NotBlank
  private String title;

  @NotBlank
  private String description;
}
