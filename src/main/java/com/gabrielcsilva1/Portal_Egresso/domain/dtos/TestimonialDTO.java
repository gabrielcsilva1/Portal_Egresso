package com.gabrielcsilva1.Portal_Egresso.domain.dtos;

import java.util.UUID;

import com.gabrielcsilva1.Portal_Egresso.infra.validation.ValidUUID;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestimonialDTO {
  @NotNull
  @ValidUUID
  private UUID egressId;

  private String text;
}
