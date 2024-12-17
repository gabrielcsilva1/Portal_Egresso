package com.gabrielcsilva1.Portal_Egresso.domain.dtos.egress;

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
public class EgressFilterResponse {
  private UUID id;
  private List<String> courses;
  private String name;
  private String avatarUrl;
}
