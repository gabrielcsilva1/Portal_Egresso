package com.gabrielcsilva1.Portal_Egresso.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.TestimonialDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Testimonial;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.EgressRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.TestimonialRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.EgressNotFoundException;

@Service
public class TestimonialService {
  @Autowired
  private TestimonialRepository testimonialRepository;

  @Autowired
  private EgressRepository egressRepository;

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

  public Page<Testimonial> fetchTestimonials(Integer year, int page, int size) {
    if (size > 20) {
      size = 20;
    }

    Pageable pageable = PageRequest.of(page, size);

    Page<Testimonial> testimonials;

    if (year == null) {
      testimonials = this.testimonialRepository.findAllByOrderByCreatedAtDesc(pageable);
    }
    else {
      testimonials = this.testimonialRepository.findByYear(year, pageable);
    }

    return testimonials;
  }
}
