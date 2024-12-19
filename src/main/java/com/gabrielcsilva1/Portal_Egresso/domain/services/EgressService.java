package com.gabrielcsilva1.Portal_Egresso.domain.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.EgressDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.egress.UpdateEgressDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Egress;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.EgressRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.EgressAlreadyExistsException;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.EgressNotFoundException;


@Service
public class EgressService {
  @Autowired
  private EgressRepository egressRepository;

  public Egress createEgress(EgressDTO egressDTO) {
    var egressWithSameEmail = this.egressRepository.findByEmail(egressDTO.getEmail());

    if (egressWithSameEmail.isPresent()) {
      throw new EgressAlreadyExistsException("email", egressDTO.getEmail());
    }

    Egress egress = new Egress(egressDTO);

    return this.egressRepository.save(egress);
  }

  public Egress getEgressById(UUID id) {
    Egress egress = this.egressRepository.findById(id)
      .orElseThrow(() -> new EgressNotFoundException());

    return egress;
  }

  public Page<Egress> fetchEgresses(Specification<Egress> queryFilters, Integer page) {
    Pageable pageable = PageRequest.of(page, 20);

    return this.egressRepository.findAll(queryFilters, pageable);
  }

  public Egress updateEgress(UUID id, UpdateEgressDTO egressDTO) {
    var egress = this.egressRepository.findById(id)
      .orElseThrow( () -> new EgressNotFoundException());

    egress.setName(egressDTO.getName());
    egress.setEmail(egressDTO.getEmail());
    egress.setDescription(egressDTO.getDescription());
    egress.setAvatarUrl(egressDTO.getAvatarUrl());
    egress.setLinkedin(egressDTO.getLinkedin());
    egress.setInstagram(egressDTO.getInstagram());
    egress.setCurriculum(egressDTO.getCurriculum());

    return this.egressRepository.save(egress);
  }

  public void deleteEgress(UUID id) {
    var egress = this.egressRepository.findById(id)
      .orElseThrow(() -> new EgressNotFoundException());

    this.egressRepository.delete(egress);
  }
}
