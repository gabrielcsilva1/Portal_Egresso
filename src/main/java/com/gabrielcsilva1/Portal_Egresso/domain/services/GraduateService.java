package com.gabrielcsilva1.Portal_Egresso.domain.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.GraduateDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.graduate.UpdateGraduateDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Graduate;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.GraduateRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.GraduateAlreadyExistsException;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.GraduateNotFoundException;


@Service
public class GraduateService {
  @Autowired
  private GraduateRepository graduateRepository;

  public Graduate createGraduate(GraduateDTO graduateDTO) {
    var graduateWithSameEmail = this.graduateRepository.findByEmail(graduateDTO.getEmail());

    if (graduateWithSameEmail.isPresent()) {
      throw new GraduateAlreadyExistsException("email", graduateDTO.getEmail());
    }

    Graduate graduate = new Graduate(graduateDTO);

    return this.graduateRepository.save(graduate);
  }

  public Graduate getGraduateById(UUID id) {
    Graduate graduate = this.graduateRepository.findById(id)
      .orElseThrow(() -> new GraduateNotFoundException());

    return graduate;
  }

  public Page<Graduate> fetchGraduates(Specification<Graduate> queryFilters, Integer page) {
    Pageable pageable = PageRequest.of(page, 20);

    return this.graduateRepository.findAll(queryFilters, pageable);
  }

  public Graduate updateGraduate(UUID id, UpdateGraduateDTO graduateDTO) {
    var graduate = this.graduateRepository.findById(id)
      .orElseThrow( () -> new GraduateNotFoundException());

    graduate.setName(graduateDTO.getName());
    graduate.setEmail(graduateDTO.getEmail());
    graduate.setDescription(graduateDTO.getDescription());
    graduate.setAvatarUrl(graduateDTO.getAvatarUrl());
    graduate.setLinkedin(graduateDTO.getLinkedin());
    graduate.setInstagram(graduateDTO.getInstagram());
    graduate.setCurriculum(graduateDTO.getCurriculum());

    return this.graduateRepository.save(graduate);
  }

  public void deleteGraduate(UUID id) {
    var graduate = this.graduateRepository.findById(id)
      .orElseThrow(() -> new GraduateNotFoundException());

    this.graduateRepository.delete(graduate);
  }
}
