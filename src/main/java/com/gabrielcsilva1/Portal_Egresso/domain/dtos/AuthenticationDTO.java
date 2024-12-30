package com.gabrielcsilva1.Portal_Egresso.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticationDTO {
  private String login;
  private String password;
}
