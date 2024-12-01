package com.gabrielcsilva1.Portal_Egresso.domain.dtos;

import java.util.UUID;

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
  private UUID egressId;

  private String text;
}
