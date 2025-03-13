package com.gabrielcsilva1.Portal_Egresso.infra.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Graduate;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Position;
import com.gabrielcsilva1.Portal_Egresso.domain.services.PositionService;
import com.gabrielcsilva1.Portal_Egresso.dtos.request.position.RequestCreatePositionJson;
import com.gabrielcsilva1.Portal_Egresso.dtos.request.position.RequestUpdatePositionJson;
import com.gabrielcsilva1.Portal_Egresso.dtos.response.position.ResponsePositionJson;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/position")
@Tag(name = "Position", description = "Manages the graduate's positions")
public class PositionController {
  @Autowired
  private PositionService positionService;

  @PostMapping
  @PreAuthorize("hasRole('GRADUATE')")
  @ApiResponse( responseCode = "201", description = "Register graduate position")
  public ResponseEntity<ResponsePositionJson> registerGraduatePosition(@Valid @RequestBody RequestCreatePositionJson positionDTO) {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    Graduate graduate = (Graduate) authentication.getPrincipal();

    Position position = this.positionService.registerGraduatePosition(graduate.getId(), positionDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(
      ResponsePositionJson.toResponse(position)
    );
  }

  @PutMapping("/{positionId}")
  @PreAuthorize("hasRole('GRADUATE')")
  @ApiResponse( responseCode = "204", description = "Graduate position updated")
  public ResponseEntity<Void> updateGraduatePosition(
    @PathVariable UUID positionId, 
    @Valid @RequestBody RequestUpdatePositionJson positionDTO
    ) {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    Graduate graduate = (Graduate) authentication.getPrincipal();

    this.positionService.updateGraduatePosition(positionId, graduate.getId(), positionDTO);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
  }

  @DeleteMapping("/{positionId}")
  @PreAuthorize("hasRole('GRADUATE')")
  @ApiResponse( responseCode = "204", description = "Graduate position deleted")
  public ResponseEntity<Void> deleteGraduatePosition(@PathVariable UUID positionId) {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    Graduate graduate = (Graduate) authentication.getPrincipal();
    
    this.positionService.deleteGraduatePosition(positionId, graduate.getId());
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
  }

  @GetMapping("/graduate/{graduateId}")
  @ApiResponse(responseCode = "200")
  public ResponseEntity<List<ResponsePositionJson>> fetchGraduatePositions(@PathVariable UUID graduateId) {
    List<Position> positions = this.positionService.fetchGraduatePositions(graduateId);

    var responseJson = positions.stream().map(ResponsePositionJson::toResponse).toList();

    return ResponseEntity.ok(responseJson);
  }
}
