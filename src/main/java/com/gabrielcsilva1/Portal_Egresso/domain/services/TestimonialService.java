package com.gabrielcsilva1.Portal_Egresso.domain.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.TestimonialDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Testimonial;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.GraduateRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.TestimonialRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.GraduateNotFoundException;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.ResourceNotFoundException;

@Service
public class TestimonialService {
  @Autowired
  private TestimonialRepository testimonialRepository;

  @Autowired
  private GraduateRepository graduateRepository;

  public Testimonial registerGraduateTestimonial(TestimonialDTO testimonialDTO) {
    var graduate = this.graduateRepository.findById(testimonialDTO.getGraduateId());

    if (graduate.isEmpty()) {
      throw new GraduateNotFoundException();
    }

    var testimonial = Testimonial.builder()
    .graduate(graduate.get())
    .text(testimonialDTO.getText())
    .build();

    return this.testimonialRepository.save(testimonial);
  }

  public Page<Testimonial> fetchTestimonials(Integer year, int page, int size) {
    if (size > 20) {
      size = 20;
    }

    if (page < 0) {
      page = 0;
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

  public Testimonial updateGraduateTestimonial(UUID testimonialId, String text) {
    Testimonial testimonial = this.testimonialRepository.findById(testimonialId)
      .orElseThrow(() -> new ResourceNotFoundException());
    
    testimonial.setText(text);
    return this.testimonialRepository.save(testimonial);
  }

  public void deleteGraduateTestimonial(UUID testimonialId) {
    Testimonial testimonial = this.testimonialRepository.findById(testimonialId)
      .orElseThrow(() -> new ResourceNotFoundException());
    
    this.testimonialRepository.delete(testimonial);
  }
}
