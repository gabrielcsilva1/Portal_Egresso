package com.gabrielcsilva1.Portal_Egresso.dtos.request.position;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestCreatePositionJson {  
  @NotBlank
  private String description;

  @NotBlank
  private String location;

  @Positive
  @NotNull
  private Integer startYear;

  private Integer endYear;
}
