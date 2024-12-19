package com.gabrielcsilva1.Portal_Egresso.infra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.EgressDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Egress;
import com.gabrielcsilva1.Portal_Egresso.domain.services.EgressService;
import com.gabrielcsilva1.Portal_Egresso.infra.presenters.EgressPresenter;
import com.gabrielcsilva1.Portal_Egresso.infra.queryfilters.EgressQueryFilter;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/egress")
public class EgressController { 
  @Autowired
  EgressService egressService;

  @GetMapping
  public ResponseEntity<Object> fetchEgresses(
    @ModelAttribute EgressQueryFilter filters,
    @RequestParam(defaultValue = "0") Integer page
    ) {
    Page<Egress> filteredEgresses = this.egressService.fetchEgresses(filters.toSpecification(), page);

    var egressPresenter = filteredEgresses.getContent().stream()
    .map(EgressPresenter::toEgressFilterResponse)
    .toList();


    var response = new PageImpl<>(egressPresenter, filteredEgresses.getPageable(), filteredEgresses.getTotalElements());

    return ResponseEntity.ok(
      response
    );
  }

  @PostMapping
  public ResponseEntity<Object> createEgress(@Valid @RequestBody EgressDTO egressDTO) {
    this.egressService.createEgress(egressDTO);

    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }
}
