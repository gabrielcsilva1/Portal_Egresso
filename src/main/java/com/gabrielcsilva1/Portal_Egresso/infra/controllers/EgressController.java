package com.gabrielcsilva1.Portal_Egresso.infra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.EgressDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.usecases.CreateEgressUseCase;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/egress")
public class EgressController { 
  @Autowired
  CreateEgressUseCase createEgressUseCase;

  @GetMapping
  public String get() {
    return "Hello";
  }

  @PostMapping
  public ResponseEntity<Object> save(@Valid @RequestBody EgressDTO egressDTO) {
    this.createEgressUseCase.execute(egressDTO);

    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }
}
