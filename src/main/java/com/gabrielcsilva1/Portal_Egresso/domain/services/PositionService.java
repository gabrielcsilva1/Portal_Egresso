package com.gabrielcsilva1.Portal_Egresso.domain.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.PositionDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.position.UpdatePositionDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Position;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.GraduateRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.PositionRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.GraduateNotFoundException;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.InvalidEndYearException;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.ResourceNotFoundException;

@Service
public class PositionService {
  @Autowired
  private PositionRepository positionRepository;

  @Autowired
  private GraduateRepository graduateRepository;
  
  public Position registerGraduatePosition(PositionDTO positionDTO) {
    var graduate = this.graduateRepository.findById(positionDTO.getGraduateId());

    if (graduate.isEmpty()) {
      throw new GraduateNotFoundException();
    }

    boolean isStartYearGreaterThanEndYear = false;

    if (positionDTO.getEndYear() != null) {
      isStartYearGreaterThanEndYear = positionDTO.getStartYear().intValue() > positionDTO.getEndYear().intValue();
    }

    if (isStartYearGreaterThanEndYear) {
      throw new InvalidEndYearException();
    }

    var position = Position.builder()
      .graduate(graduate.get())
      .description(positionDTO.getDescription())
      .location(positionDTO.getLocation())
      .startYear(positionDTO.getStartYear())
      .endYear(positionDTO.getEndYear())
      .build();
    
    return this.positionRepository.save(position);
  }

  public Position updateGraduatePosition(UUID positionId, UpdatePositionDTO positionDTO) {
    Position position = this.positionRepository.findById(positionId)
      .orElseThrow(() -> new ResourceNotFoundException());

    position.setDescription(positionDTO.getDescription());
    position.setLocation(positionDTO.getLocation());
    position.setStartYear(positionDTO.getStartYear());
    position.setEndYear(positionDTO.getEndYear());

    return this.positionRepository.save(position);
  }

  public void deleteGraduatePosition(UUID positionId) {
    Position position = this.positionRepository.findById(positionId)
      .orElseThrow(() -> new ResourceNotFoundException());

    this.positionRepository.delete(position);
  }
}
