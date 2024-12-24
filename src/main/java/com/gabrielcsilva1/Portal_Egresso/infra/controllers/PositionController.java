package com.gabrielcsilva1.Portal_Egresso.infra.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.PositionDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.position.UpdatePositionDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.services.PositionService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/graduate/position")
@Tag(name = "Position", description = "Manages the graduate's positions")
public class PositionController {
  @Autowired
  private PositionService positionService;

  @PostMapping
  @ApiResponse( responseCode = "201", description = "Register graduate position")
  public ResponseEntity<Object> registerGraduatePosition(@Valid @RequestBody PositionDTO positionDTO) {
    this.positionService.registerGraduatePosition(positionDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }

  @PutMapping("/{positionId}")
  @ApiResponse( responseCode = "204", description = "Graduate position updated")
  public ResponseEntity<Object> updateGraduatePosition(
    @PathVariable UUID positionId, 
    @Valid @RequestBody UpdatePositionDTO positionDTO
    ) {
    this.positionService.updateGraduatePosition(positionId, positionDTO);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
  }

  @DeleteMapping("/{positionId}")
  @ApiResponse( responseCode = "204", description = "Graduate position deleted")
  public ResponseEntity<Object> deleteGraduatePosition(@PathVariable UUID positionId) {
    this.positionService.deleteGraduatePosition(positionId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
  }
}
