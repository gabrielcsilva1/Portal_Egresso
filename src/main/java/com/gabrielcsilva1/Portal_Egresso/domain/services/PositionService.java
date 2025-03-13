package com.gabrielcsilva1.Portal_Egresso.domain.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Position;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.GraduateRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.PositionRepository;
import com.gabrielcsilva1.Portal_Egresso.dtos.request.position.RequestCreatePositionJson;
import com.gabrielcsilva1.Portal_Egresso.dtos.request.position.RequestUpdatePositionJson;
import com.gabrielcsilva1.Portal_Egresso.exeptions.GraduateNotFoundException;
import com.gabrielcsilva1.Portal_Egresso.exeptions.InvalidEndYearException;
import com.gabrielcsilva1.Portal_Egresso.exeptions.ResourceNotFoundException;
import com.gabrielcsilva1.Portal_Egresso.exeptions.core.BadRequestException;

@Service
public class PositionService {
  @Autowired
  private PositionRepository positionRepository;

  @Autowired
  private GraduateRepository graduateRepository;
  
  public Position registerGraduatePosition(UUID graduateId, RequestCreatePositionJson positionDTO) {
    var graduate = this.graduateRepository.findById(graduateId);

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

  public Position updateGraduatePosition(UUID positionId, UUID graduateId, RequestUpdatePositionJson positionDTO) {
    Position position = this.positionRepository.findById(positionId)
      .orElseThrow(() -> new ResourceNotFoundException());

    boolean isSameGraduateId = position.getGraduate().getId().equals(graduateId);
    if (isSameGraduateId == false) {
      throw new BadRequestException();
    }

    position.setDescription(positionDTO.getDescription());
    position.setLocation(positionDTO.getLocation());
    position.setStartYear(positionDTO.getStartYear());
    position.setEndYear(positionDTO.getEndYear());

    return this.positionRepository.save(position);
  }

  public void deleteGraduatePosition(UUID positionId, UUID graduateId) {
    Position position = this.positionRepository.findById(positionId)
      .orElseThrow(() -> new ResourceNotFoundException());

    boolean isSameGraduateId = position.getGraduate().getId().equals(graduateId);
    if (isSameGraduateId == false) {
      throw new BadRequestException();
    }

    this.positionRepository.delete(position);
  }

  public List<Position> fetchGraduatePositions(UUID graduateId) {
    return this.positionRepository.findByGraduateId(graduateId);
  }
}
