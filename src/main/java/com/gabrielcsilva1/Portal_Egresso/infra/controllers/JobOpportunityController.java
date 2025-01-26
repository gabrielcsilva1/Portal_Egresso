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
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.jobOpportunity.JobOpportunityResponse;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.jobOpportunity.UpdateJobOpportunityDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.JobOpportunity;
import com.gabrielcsilva1.Portal_Egresso.domain.services.JobOpportunityService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/job-opportunity")
@Tag(name = "Job Opportunity")
public class JobOpportunityController {
  @Autowired
  private JobOpportunityService jobOpportunityService;
  
  @PostMapping
  @ApiResponse( responseCode = "201", description = "Create job opportunity")
  public ResponseEntity<JobOpportunityResponse> create(@Valid @RequestBody JobOpportunityDTO dto) {
    JobOpportunity jobOpportunity = jobOpportunityService.create(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(
      JobOpportunityResponse.toResponse(jobOpportunity)
    );
  }

  @PutMapping("/{id}")
  @ApiResponse( responseCode = "204", description = "Update job opportunity")
  public ResponseEntity<Void> update(
    @PathVariable UUID id,
    @Valid @RequestBody UpdateJobOpportunityDTO dto) {
    jobOpportunityService.update(id, dto);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
  }

  @DeleteMapping("/{id}")
  @ApiResponse( responseCode = "204", description = "Delete job opportunity")
  public ResponseEntity<Void> delete(@PathVariable UUID jobId) {
    jobOpportunityService.delete(jobId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
  }

  @GetMapping("/verified")
  @ApiResponse( responseCode = "200", description = "List verified job opportunities")
  public ResponseEntity<List<JobOpportunityResponse>> fetchVerifiedJobOpportunities() {
    var jobsOpportunity = jobOpportunityService.fetchVerifiedJobOpportunities();

    List<JobOpportunityResponse> response = jobsOpportunity.stream()
      .map(JobOpportunityResponse::toResponse)
      .toList();

    return ResponseEntity.ok(response);
  }

  @GetMapping("/unverified")
  @ApiResponse( responseCode = "200", description = "List unverified job opportunities")
  public ResponseEntity<List<JobOpportunityResponse>> fetchUnverifiedJobOpportunities() {
    var jobsOpportunity = jobOpportunityService.fetchUnverifiedJobOpportunities();

    List<JobOpportunityResponse> response = jobsOpportunity.stream()
      .map(JobOpportunityResponse::toResponse)
      .toList();

    return ResponseEntity.ok(response);
  }
}
