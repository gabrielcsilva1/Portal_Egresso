package com.gabrielcsilva1.Portal_Egresso.domain.usecases;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.PositionDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Position;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.EgressRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.PositionRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.usecases.exeptions.EgressNotFoundException;
import com.gabrielcsilva1.Portal_Egresso.domain.usecases.exeptions.InvalidEndYearException;

@Service
public class CreatePositionUseCase {
  @Autowired
  private PositionRepository positionRepository;

  @Autowired
  private EgressRepository egressRepository;

  public Position execute(PositionDTO positionDTO) {
    var egress = this.egressRepository.findById(positionDTO.getEgressId());

    if (egress.isEmpty()) {
      throw new EgressNotFoundException();
    }

    boolean isStartYearGreaterThanEndYear = false;
    boolean isEndYearGreaterThanCurrentYear = false;

    if (positionDTO.getEndYear() != null) {
      isStartYearGreaterThanEndYear = positionDTO.getStartYear().intValue() > positionDTO.getEndYear().intValue();
      isEndYearGreaterThanCurrentYear = positionDTO.getEndYear().intValue() > LocalDate.now().getYear();
    }

    if (isStartYearGreaterThanEndYear || isEndYearGreaterThanCurrentYear) {
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
}
