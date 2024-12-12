package com.gabrielcsilva1.Portal_Egresso.infra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.PositionDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.services.PositionService;

import jakarta.validation.Valid;

@RestController
public class PositionController {
  @Autowired
  private PositionService positionService;

  @PostMapping
  @RequestMapping("/egress/position")
  public ResponseEntity<Object> registerEgressPosition(@Valid @RequestBody PositionDTO positionDTO) {
    this.positionService.registerEgressPosition(positionDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }
}
