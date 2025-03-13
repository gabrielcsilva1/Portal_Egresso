package com.gabrielcsilva1.Portal_Egresso.dtos.request.graduate;

import java.util.UUID;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Graduate;
import com.gabrielcsilva1.Portal_Egresso.infra.validation.ValidUUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestCreateGraduateJson {
  @NotBlank
  private String name;

  @NotBlank
  @Email
  private String email;

  @NotBlank
  private String password;
  
  @ValidUUID
  private UUID courseId;

  @NotNull
  @Positive
  private Integer startYear;

  private Integer endYear;
  
  public Graduate toEntity() {
    return Graduate.builder()
        .name(name)
        .email(email)
        .password(password)
        .build();
  }
}
