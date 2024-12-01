package com.gabrielcsilva1.Portal_Egresso.domain.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.TestimonialDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Testimonial;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.EgressRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.TestimonialRepository;
import com.gabrielcsilva1.Portal_Egresso.exeptions.EgressNotFoundException;

@Service
public class CreateEgressTestimonialUseCase {
  @Autowired
  private TestimonialRepository testimonialRepository;

  @Autowired
  private EgressRepository egressRepository;

  public Testimonial execute(TestimonialDTO testimonialDTO) {
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
