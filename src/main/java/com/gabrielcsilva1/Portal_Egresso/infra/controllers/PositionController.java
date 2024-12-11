package com.gabrielcsilva1.Portal_Egresso.infra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.PositionDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.usecases.CreatePositionUseCase;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/position")
public class PositionController {
  @Autowired
  private CreatePositionUseCase createPositionUseCase;

  @PostMapping
  public ResponseEntity<Object> save(@Valid @RequestBody PositionDTO positionDTO) {
    this.createPositionUseCase.execute(positionDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }
}
