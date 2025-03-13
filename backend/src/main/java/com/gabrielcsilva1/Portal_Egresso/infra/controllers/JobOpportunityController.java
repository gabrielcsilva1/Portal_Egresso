package com.gabrielcsilva1.Portal_Egresso.infra.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Graduate;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.JobOpportunity;
import com.gabrielcsilva1.Portal_Egresso.domain.services.JobOpportunityService;
import com.gabrielcsilva1.Portal_Egresso.dtos.paginated.ResponsePaginated;
import com.gabrielcsilva1.Portal_Egresso.dtos.request.RequestUpdateRegisterStatusJson;
import com.gabrielcsilva1.Portal_Egresso.dtos.request.jobOpportunity.RequestCreateJobOpportunityJson;
import com.gabrielcsilva1.Portal_Egresso.dtos.request.jobOpportunity.RequestUpdateJobOpportunityJson;
import com.gabrielcsilva1.Portal_Egresso.dtos.response.jobOpportunity.ResponseJobOpportunityJson;
import com.gabrielcsilva1.Portal_Egresso.dtos.response.jobOpportunity.ResponseShortJobOpportunityJson;

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
  @PreAuthorize("hasRole('GRADUATE')")
  @ApiResponse( responseCode = "201", description = "Create job opportunity")
  public ResponseEntity<ResponseJobOpportunityJson> create(@RequestBody RequestCreateJobOpportunityJson dto) {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    Graduate graduate = (Graduate) authentication.getPrincipal();
    
    JobOpportunity jobOpportunity = jobOpportunityService.createJobOpportunity(graduate.getId(), dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(
      ResponseJobOpportunityJson.toResponse(jobOpportunity)
    );
  }

  @PutMapping("/{id}")
  @ApiResponse( responseCode = "204", description = "Update job opportunity")
  public ResponseEntity<Void> update(
    @PathVariable UUID id,
    @Valid @RequestBody RequestUpdateJobOpportunityJson dto) {
    jobOpportunityService.updateJobOpportunity(id, dto);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
  }

  @PutMapping("/{id}/status")
  @PreAuthorize("hasRole('COORDINATOR')")
  @ApiResponse( responseCode = "204", description = "Update job opportunity")
  public ResponseEntity<Void> updateRegisterStatus(
    @PathVariable UUID id,
    @Valid @RequestBody RequestUpdateRegisterStatusJson dto) {
    jobOpportunityService.updateJobOpportunityRegisterStatus(id, dto.getNewStatus());
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
  }

  @DeleteMapping("/{id}")
  @ApiResponse( responseCode = "204", description = "Delete job opportunity")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    jobOpportunityService.deleteJobOpportunity(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
  }

  @GetMapping
  @ApiResponse( responseCode = "200", description = "List verified job opportunities")
  public ResponseEntity<ResponsePaginated<ResponseJobOpportunityJson>> fetchVerifiedJobOpportunities(@RequestParam(defaultValue = "0") int pageIndex) {
    Page<JobOpportunity> pageResponse = jobOpportunityService.fetchVerifiedJobOpportunities(pageIndex);

    List<ResponseJobOpportunityJson> response = pageResponse.stream()
      .map(ResponseJobOpportunityJson::toResponse)
      .toList();

    var responsePaginatedJson = new ResponsePaginated<>(
      response, 
      pageResponse.getNumber(), // índice da página
      pageResponse.getTotalPages(), 
      pageResponse.getTotalElements()
    );

    return ResponseEntity.ok(responsePaginatedJson);
  }

  @GetMapping("/unverified")
  @PreAuthorize("hasRole('COORDINATOR')")
  @ApiResponse( responseCode = "200", description = "List unverified job opportunities")
  public ResponseEntity<ResponsePaginated<ResponseJobOpportunityJson>> fetchUnverifiedJobOpportunities(@RequestParam(defaultValue = "0") int pageIndex) {
    Page<JobOpportunity> pageResponse = jobOpportunityService.fetchUnverifiedJobOpportunities(pageIndex);

    List<ResponseJobOpportunityJson> response = pageResponse.stream()
      .map(ResponseJobOpportunityJson::toResponse)
      .toList();

    var responsePaginatedJson = new ResponsePaginated<>(
      response, 
      pageResponse.getNumber(), // índice da página
      pageResponse.getTotalPages(), 
      pageResponse.getTotalElements()
      );

    return ResponseEntity.ok(responsePaginatedJson);
  }

  @GetMapping("/graduate/{graduateId}")
  @ApiResponse(responseCode = "200")
  public ResponseEntity<List<ResponseShortJobOpportunityJson>> fetchGraduateTestimonials(
    @PathVariable UUID graduateId
    ) {
    List<JobOpportunity> testimonialsList = this.jobOpportunityService.fetchGraduateJobOpportunity(graduateId);

    List<ResponseShortJobOpportunityJson> responseJson = testimonialsList
      .stream()
      .map(ResponseShortJobOpportunityJson::toResponse)
      .toList();

    return ResponseEntity.ok(responseJson);
  }
}
