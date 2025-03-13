package com.gabrielcsilva1.Portal_Egresso.dtos.request.authentication;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RequestCoordinatorAuthenticationJson {
  private String login;
  private String password;
}
