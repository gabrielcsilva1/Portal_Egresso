package com.gabrielcsilva1.Portal_Egresso.domain.dtos.graduate;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FetchGraduateResponse {
  private UUID id;
  private List<String> courses;
  private List<String> positions;
  private String name;
  private String avatarUrl;
}
