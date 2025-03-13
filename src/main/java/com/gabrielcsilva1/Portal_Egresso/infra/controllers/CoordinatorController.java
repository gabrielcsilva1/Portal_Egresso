package com.gabrielcsilva1.Portal_Egresso.infra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabrielcsilva1.Portal_Egresso.domain.services.CoordinatorService;
import com.gabrielcsilva1.Portal_Egresso.dtos.request.authentication.RequestCoordinatorAuthenticationJson;
import com.gabrielcsilva1.Portal_Egresso.dtos.request.coordinator.RequestCoordinatorJson;
import com.gabrielcsilva1.Portal_Egresso.dtos.response.authentication.ResponseCoordinatorAuthenticationJson;
import com.gabrielcsilva1.Portal_Egresso.dtos.response.coordinator.ResponseCoordinatorJson;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/coordinator")
@Tag(name = "Coordinator")
public class CoordinatorController {
  @Autowired
  private CoordinatorService coordinatorService;

  @PostMapping("/session")
  @ApiResponse(responseCode = "200", description = "Login was successful")
  public ResponseEntity<ResponseCoordinatorAuthenticationJson> login(@Valid @RequestBody RequestCoordinatorAuthenticationJson authenticationDTO, HttpServletResponse response) {
    String token = this.coordinatorService.login(
      authenticationDTO.getLogin(),
      authenticationDTO.getPassword()
      );
    
    ResponseCookie cookie = ResponseCookie.from("jwtToken", token)
      .httpOnly(true)
      .secure(true)
      .path("/")
      .build();
    
    response.addHeader("Set-Cookie", cookie.toString());

    ResponseCoordinatorAuthenticationJson authenticationResponse = new ResponseCoordinatorAuthenticationJson(token);

    return ResponseEntity.status(HttpStatus.OK).body(authenticationResponse);
  }

  @PostMapping
  @ApiResponse(responseCode = "200", description = "Create a new Coordinator")
  public ResponseEntity<ResponseCoordinatorJson> create(@Valid @RequestBody RequestCoordinatorJson coordinatorDTO) {
    var coordinator = this.coordinatorService.create(coordinatorDTO);

    var responseJson = ResponseCoordinatorJson.toResponse(coordinator);
    return ResponseEntity.status(HttpStatus.CREATED).body(responseJson);
  }
}
