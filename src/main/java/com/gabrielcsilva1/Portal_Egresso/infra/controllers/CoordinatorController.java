package com.gabrielcsilva1.Portal_Egresso.infra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.AuthenticationDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.CoordinatorDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.authentication.AuthenticationResponse;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Coordinator;
import com.gabrielcsilva1.Portal_Egresso.domain.services.CoordinatorService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/coordinator")
@Tag(name = "Coordinator")
public class CoordinatorController {
  @Autowired
  private CoordinatorService coordinatorService;

  @PostMapping("/session")
  @ApiResponse(responseCode = "200", description = "Login was successful")
  public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthenticationDTO authenticationDTO) {
    String token = this.coordinatorService.authenticate(
      authenticationDTO.getLogin(),
      authenticationDTO.getPassword()
      );

    AuthenticationResponse response = new AuthenticationResponse(token);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @PostMapping
  @ApiResponse(responseCode = "200", description = "Create a new Coordinator")
  public ResponseEntity<Coordinator> create(@Valid @RequestBody CoordinatorDTO coordinatorDTO) {
    var coordinator = this.coordinatorService.create(coordinatorDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(coordinator);
  }
}
