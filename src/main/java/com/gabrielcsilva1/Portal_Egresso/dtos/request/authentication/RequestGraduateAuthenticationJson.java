package com.gabrielcsilva1.Portal_Egresso.dtos.request.authentication;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RequestGraduateAuthenticationJson {
  private String email;
  private String password;
}
