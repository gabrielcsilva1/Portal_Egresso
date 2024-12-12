package com.gabrielcsilva1.Portal_Egresso.domain.services;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.EgressDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.PositionDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.TestimonialDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Egress;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Position;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Testimonial;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.EgressRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.PositionRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.TestimonialRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.EgressAlreadyExistsException;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.EgressNotFoundException;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.InvalidEndYearException;

@Service
public class EgressService {
  @Autowired
  private EgressRepository egressRepository;

  @Autowired
  private PositionRepository positionRepository;

  @Autowired TestimonialRepository testimonialRepository;

  public Egress createEgress(EgressDTO egressDTO) {
    var egressWithSameEmail = this.egressRepository.findByEmail(egressDTO.getEmail());

    if (egressWithSameEmail.isPresent()) {
      throw new EgressAlreadyExistsException("email", egressDTO.getEmail());
    }

    Egress egress = new Egress(egressDTO);

    return this.egressRepository.save(egress);
  }

  public Position registerEgressPosition(PositionDTO positionDTO) {
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

  public Testimonial registerEgressTestimonial(TestimonialDTO testimonialDTO) {
    var egress = this.egressRepository.findById(testimonialDTO.getEgressId());

    if (egress.isEmpty()) {
      throw new EgressNotFoundException();
    }

    var testimonial = Testimonial.builder()
    .egress(egress.get())
    .text(testimonialDTO.getText())
    .build();

    return this.testimonialRepository.save(testimonial);
  }
}
