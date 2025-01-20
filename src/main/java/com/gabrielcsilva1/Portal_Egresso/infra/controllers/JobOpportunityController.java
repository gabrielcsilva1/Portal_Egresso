package com.gabrielcsilva1.Portal_Egresso.infra.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.JobOpportunityDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.jobOpportunity.FetchJobOpportunityResponseDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.jobOpportunity.UpdateJobOpportunityDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.services.JobOpportunityService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/jobOpportunity")
public class JobOpportunityController {
  @Autowired
  private JobOpportunityService jobOpportunityService;
  
  @PostMapping
  public ResponseEntity<Void> create(@Valid @RequestBody JobOpportunityDTO dto) {
    jobOpportunityService.create(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Void> update(
    @PathVariable UUID id,
    @Valid @RequestBody UpdateJobOpportunityDTO dto) {
    jobOpportunityService.update(id, dto);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID jobId) {
    jobOpportunityService.delete(jobId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
  }

  @GetMapping("/verified")
  public ResponseEntity<List<FetchJobOpportunityResponseDTO>> fetchVerifiedJobOpportunities() {
    var jobsOpportunity = jobOpportunityService.fetchVerifiedJobOpportunities();

    List<FetchJobOpportunityResponseDTO> response = jobsOpportunity.stream()
      .map(FetchJobOpportunityResponseDTO::toResponse)
      .toList();

    return ResponseEntity.ok(response);
  }

  @GetMapping("/unverified")
  public ResponseEntity<List<FetchJobOpportunityResponseDTO>> fetchUnverifiedJobOpportunities() {
    var jobsOpportunity = jobOpportunityService.fetchUnverifiedJobOpportunities();

    List<FetchJobOpportunityResponseDTO> response = jobsOpportunity.stream()
      .map(FetchJobOpportunityResponseDTO::toResponse)
      .toList();

    return ResponseEntity.ok(response);
  }
}
