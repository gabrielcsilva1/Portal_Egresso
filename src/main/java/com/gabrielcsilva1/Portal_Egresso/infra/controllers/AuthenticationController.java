package com.gabrielcsilva1.Portal_Egresso.infra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.AuthenticationDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.authentication.AuthenticationResponse;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Coordinator;
import com.gabrielcsilva1.Portal_Egresso.domain.services.TokenService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
public class AuthenticationController {
  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private TokenService tokenService;

  @PostMapping("coordinator/session")
  @Tag(name = "Coordinator")
  @ApiResponse(responseCode = "200", description = "Login was successful")
  public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationDTO authenticationDTO) {
    var usernamePassword = new UsernamePasswordAuthenticationToken(authenticationDTO.getLogin(), authenticationDTO.getPassword());

    var auth = this.authenticationManager.authenticate(usernamePassword);

    var token = this.tokenService.generateToken((Coordinator) auth.getPrincipal());

    AuthenticationResponse response = new AuthenticationResponse(token);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
