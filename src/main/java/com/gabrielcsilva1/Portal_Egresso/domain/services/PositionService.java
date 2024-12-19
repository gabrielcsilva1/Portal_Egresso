package com.gabrielcsilva1.Portal_Egresso.domain.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.PositionDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.position.UpdatePositionDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Position;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.EgressRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.PositionRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.EgressNotFoundException;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.InvalidEndYearException;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.ResourceNotFoundException;

@Service
public class PositionService {
  @Autowired
  private PositionRepository positionRepository;

  @Autowired
  private EgressRepository egressRepository;
  
  public Position registerEgressPosition(PositionDTO positionDTO) {
    var egress = this.egressRepository.findById(positionDTO.getEgressId());

    if (egress.isEmpty()) {
      throw new EgressNotFoundException();
    }

    boolean isStartYearGreaterThanEndYear = false;

    if (positionDTO.getEndYear() != null) {
      isStartYearGreaterThanEndYear = positionDTO.getStartYear().intValue() > positionDTO.getEndYear().intValue();
    }

    if (isStartYearGreaterThanEndYear) {
      throw new InvalidEndYearException();
    }

    var position = Position.builder()
      .egress(egress.get())
      .description(positionDTO.getDescription())
      .location(positionDTO.getLocation())
      .startYear(positionDTO.getStartYear())
      .endYear(positionDTO.getEndYear())
      .build();
    
    return this.positionRepository.save(position);
  }

  public Position updateEgressPosition(UUID id, UpdatePositionDTO positionDTO) {
    Position position = this.positionRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException());

    position.setDescription(positionDTO.getDescription());
    position.setLocation(positionDTO.getLocation());
    position.setStartYear(positionDTO.getStartYear());
    position.setEndYear(positionDTO.getEndYear());

    return this.positionRepository.save(position);
  }

  public void deleteEgressPosition(UUID id) {
    Position position = this.positionRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException());

    this.positionRepository.delete(position);
  }
}
