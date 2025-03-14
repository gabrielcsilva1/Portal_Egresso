package com.gabrielcsilva1.Portal_Egresso.infra.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Graduate;
import com.gabrielcsilva1.Portal_Egresso.domain.services.GraduateService;
import com.gabrielcsilva1.Portal_Egresso.dtos.paginated.ResponsePaginated;
import com.gabrielcsilva1.Portal_Egresso.dtos.queryParams.QueryGraduate;
import com.gabrielcsilva1.Portal_Egresso.dtos.request.RequestUpdateRegisterStatusJson;
import com.gabrielcsilva1.Portal_Egresso.dtos.request.authentication.RequestGraduateAuthenticationJson;
import com.gabrielcsilva1.Portal_Egresso.dtos.request.graduate.RequestCreateGraduateJson;
import com.gabrielcsilva1.Portal_Egresso.dtos.request.graduate.RequestUpdateGraduateJson;
import com.gabrielcsilva1.Portal_Egresso.dtos.response.authentication.ResponseGraduateAuthenticationJson;
import com.gabrielcsilva1.Portal_Egresso.dtos.response.graduate.ResponseGraduateJson;
import com.gabrielcsilva1.Portal_Egresso.dtos.response.graduate.ResponseShortGraduateJson;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/graduate")
@Tag(name = "Graduate")
public class GraduateController { 
  @Autowired
  GraduateService graduateService;

  @GetMapping
  @ApiResponse( responseCode = "200", description = "List of Graduates")
  public ResponseEntity<ResponsePaginated<ResponseShortGraduateJson>> fetchGraduates(
    @ModelAttribute QueryGraduate filters,
    @RequestParam(defaultValue = "0") Integer pageIndex
    ) {
    Page<Graduate> filteredGraduates = this.graduateService.fetchVerifiedGraduates(filters.toSpecification(), pageIndex);

    List<ResponseShortGraduateJson> graduateShortList = filteredGraduates.getContent().stream()
    .map(ResponseShortGraduateJson::toResponse)
    .toList();


    var paginatedGraduatesJson = new ResponsePaginated<>(
      graduateShortList, 
      filteredGraduates.getNumber(), 
      filteredGraduates.getTotalPages(),
      filteredGraduates.getTotalElements()
      );

    return ResponseEntity.ok(
      paginatedGraduatesJson
    );
  }

  @GetMapping("/unverified")
  @ApiResponse( responseCode = "200")
  public ResponseEntity<ResponsePaginated<ResponseShortGraduateJson>> fetchUnverifiedGraduates(
    @RequestParam(defaultValue = "0") int pageIndex
  ) {
    Page<Graduate> pageGraduates = this.graduateService.fetchUnverifiedGraduates(pageIndex);

    List<ResponseShortGraduateJson> graduateShortList = pageGraduates.stream()
    .map(ResponseShortGraduateJson::toResponse)
    .toList();

    var responseJson = new ResponsePaginated<>(
      graduateShortList,
      pageGraduates.getNumber(),
      pageGraduates.getTotalPages(),
      pageGraduates.getTotalElements()
      );

    return ResponseEntity.ok(
      responseJson
    );
  }

  @PostMapping
  @ApiResponse( responseCode = "201", description = "Graduate created")
  public ResponseEntity<ResponseGraduateJson> createGraduate(@Valid @RequestBody RequestCreateGraduateJson graduateDTO) {
    Graduate graduate = this.graduateService.createGraduate(graduateDTO);

    return ResponseEntity.status(HttpStatus.CREATED).body(
      ResponseGraduateJson.toResponse(graduate)
      );
  }

  @PostMapping("/session")
  @ApiResponse(responseCode = "200", description = "Login was successful")
  public ResponseEntity<ResponseGraduateAuthenticationJson> login(@Valid @RequestBody RequestGraduateAuthenticationJson authenticationDTO, HttpServletResponse response) {
    var responseJson = this.graduateService.login(
      authenticationDTO.getEmail(),
      authenticationDTO.getPassword()
      );
    
    ResponseCookie cookie = ResponseCookie.from("jwtToken", responseJson.getToken())
      .httpOnly(true)
      .secure(true)
      .sameSite("None")
      .path("/")
      .build();
    
    response.addHeader("Set-Cookie", cookie.toString());

    return ResponseEntity.status(HttpStatus.OK).body(responseJson);
  }

  @GetMapping("/{id}")
  @ApiResponse( responseCode = "200", description = "Graduate founded")
  public ResponseEntity<ResponseGraduateJson> getGraduateById(@PathVariable UUID id) {
    Graduate graduate = this.graduateService.getGraduateById(id);

    return ResponseEntity.status(HttpStatus.OK).body(
      ResponseGraduateJson.toResponse(graduate)
    );
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('GRADUATE')")
  @ApiResponse( responseCode = "204", description = "Graduate updated")
  public ResponseEntity<Void> updateGraduate(
    @PathVariable UUID id, 
    @Valid @RequestBody RequestUpdateGraduateJson graduateDTO) {
      this.graduateService.updateGraduate(id, graduateDTO);

      return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

  @PutMapping("/{id}/status")
  @PreAuthorize("hasRole('COORDINATOR')")
  @ApiResponse( responseCode = "204")
  public ResponseEntity<Void> updateGraduateRegisterStatus(
    @PathVariable UUID id, 
    @Valid @RequestBody RequestUpdateRegisterStatusJson requestJson) {
      this.graduateService.updateGraduateRegisterStatus(id, requestJson.getNewStatus());

      return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('COORDINATOR')")
  @ApiResponse( responseCode = "204", description = "Graduate deleted")
  public ResponseEntity<Void> deleteGraduate(@PathVariable UUID id) {
    this.graduateService.deleteGraduate(id);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
  }
}
