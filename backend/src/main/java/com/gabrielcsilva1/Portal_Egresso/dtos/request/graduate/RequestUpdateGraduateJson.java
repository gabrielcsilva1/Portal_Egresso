package com.gabrielcsilva1.Portal_Egresso.dtos.request.graduate;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RequestUpdateGraduateJson {
  @NotBlank
  private String name;

  @NotBlank
  @Email
  private String email;

  private String description;

  @URL
  private String avatarUrl;

  private String linkedin;

  private String instagram;

  private String curriculum;
}
