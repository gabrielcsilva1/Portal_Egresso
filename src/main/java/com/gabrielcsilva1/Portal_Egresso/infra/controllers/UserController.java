package com.gabrielcsilva1.Portal_Egresso.infra.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Coordinator;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Graduate;
import com.gabrielcsilva1.Portal_Egresso.dtos.enums.RoleEnum;
import com.gabrielcsilva1.Portal_Egresso.dtos.response.graduate.ResponseShortGraduateJson;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "User")
public class UserController {
  
  @GetMapping("/me")
  public ResponseGetAuthenticatedUser getAuthenticatedUser() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null) {
      return null;
    }

    switch (authentication.getPrincipal()) {
      case Graduate graduate:
        var responseJson = ResponseShortGraduateJson.toResponse(graduate);
        return new ResponseGetAuthenticatedUser(RoleEnum.GRADUATE, responseJson);

      case Coordinator _:
        return new ResponseGetAuthenticatedUser(RoleEnum.COORDINATOR, null);

      default:
        return null;
    }
  }
}

record ResponseGetAuthenticatedUser(RoleEnum role, ResponseShortGraduateJson user) {
}
