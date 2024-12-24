package com.gabrielcsilva1.Portal_Egresso.infra.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.GraduateDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.graduate.FetchGraduateResponse;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.graduate.GetGraduateResponse;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.graduate.UpdateGraduateDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.paginated.PaginatedResponse;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Graduate;
import com.gabrielcsilva1.Portal_Egresso.domain.services.GraduateService;
import com.gabrielcsilva1.Portal_Egresso.infra.presenters.GraduatePresenter;
import com.gabrielcsilva1.Portal_Egresso.infra.queryfilters.GraduateQueryFilter;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/graduate")
@Tag(name = "Graduate")
public class GraduateController { 
  @Autowired
  GraduateService graduateService;

  @GetMapping
  @ApiResponse(
    responseCode = "200",
    description = "List of Graduates"
  )
  public ResponseEntity<PaginatedResponse<FetchGraduateResponse>> fetchGraduates(
    @ModelAttribute GraduateQueryFilter filters,
    @RequestParam(defaultValue = "1") Integer page
    ) {
    Page<Graduate> filteredGraduates = this.graduateService.fetchGraduates(filters.toSpecification(), page-1);

    List<FetchGraduateResponse> graduatePresenterList = filteredGraduates.getContent().stream()
    .map(GraduatePresenter::toFetchGraduateResponse)
    .toList();


    var paginatedGraduatePresenterList = new PaginatedResponse<>(
      graduatePresenterList, 
      filteredGraduates.getNumber(), 
      filteredGraduates.getTotalPages(),
      filteredGraduates.getTotalElements()
      );

    return ResponseEntity.ok(
      paginatedGraduatePresenterList
    );
  }

  @PostMapping
  @ApiResponse( responseCode = "201", description = "Graduate created")
  public ResponseEntity<Void> createGraduate(@Valid @RequestBody GraduateDTO graduateDTO) {
    this.graduateService.createGraduate(graduateDTO);

    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }

  @GetMapping("/{id}")
  @ApiResponse(
    responseCode = "200",
    description = "Graduate founded",
    content = {
      @Content(
        schema = @Schema(implementation = GetGraduateResponse.class)
      )
    }
  )
  public ResponseEntity<Object> getGraduateById(@PathVariable UUID id) {
    Graduate graduate = this.graduateService.getGraduateById(id);

    GetGraduateResponse graduatePresenter = GraduatePresenter.toGetGraduateResponse(graduate);

    return ResponseEntity.status(HttpStatus.OK).body(graduatePresenter);
  }

  @PutMapping("/{id}")
  @ApiResponse(
    responseCode = "204",
    description = "Graduate updated"
  )
  public ResponseEntity<Void> updateGraduate(
    @PathVariable UUID id, 
    @Valid @RequestBody UpdateGraduateDTO graduateDTO) {
      this.graduateService.updateGraduate(id, graduateDTO);

      return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

  @DeleteMapping("/{id}")
  @ApiResponse(
    responseCode = "204",
    description = "Graduate deleted"
  )
  public ResponseEntity<Void> deleteGraduate(@PathVariable UUID id) {
    this.graduateService.deleteGraduate(id);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
  }
}
