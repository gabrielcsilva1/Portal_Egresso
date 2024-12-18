package com.gabrielcsilva1.Portal_Egresso.infra.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
  public ResponseEntity<Object> fetchEgresses(@ModelAttribute EgressQueryFilter filters) {
    List<Egress> filteredEgresses = this.egressService.fetchEgresses(filters.toSpecification());

    return ResponseEntity.ok(
      filteredEgresses.stream()
      .map(EgressPresenter::toEgressFilterResponse)
      .toList()
    );
  }

  @PostMapping
  public ResponseEntity<Object> createEgress(@Valid @RequestBody EgressDTO egressDTO) {
    this.egressService.createEgress(egressDTO);

    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }
}
