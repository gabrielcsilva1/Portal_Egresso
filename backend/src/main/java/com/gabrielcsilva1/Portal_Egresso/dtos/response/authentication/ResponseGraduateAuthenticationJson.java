package com.gabrielcsilva1.Portal_Egresso.dtos.response.authentication;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Graduate;
import com.gabrielcsilva1.Portal_Egresso.dtos.response.graduate.ResponseShortGraduateJson;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseGraduateAuthenticationJson {
  private String token;
  private ResponseShortGraduateJson graduate;

  static public ResponseGraduateAuthenticationJson toResponse(String token, Graduate graduate) {
    return ResponseGraduateAuthenticationJson.builder()
      .token(token)
      .graduate(ResponseShortGraduateJson.toResponse(graduate))
      .build();
  }
}
