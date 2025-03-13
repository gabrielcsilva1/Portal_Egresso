package com.gabrielcsilva1.Portal_Egresso.domain.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Testimonial;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.GraduateRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.TestimonialRepository;
import com.gabrielcsilva1.Portal_Egresso.dtos.enums.StatusEnum;
import com.gabrielcsilva1.Portal_Egresso.dtos.request.testimonial.RequestCreateTestimonialJson;
import com.gabrielcsilva1.Portal_Egresso.exeptions.GraduateNotFoundException;
import com.gabrielcsilva1.Portal_Egresso.exeptions.ResourceNotFoundException;

@Service
public class TestimonialService {
  @Autowired
  private TestimonialRepository testimonialRepository;

  @Autowired
  private GraduateRepository graduateRepository;

  public Testimonial registerGraduateTestimonial(UUID graduateId, RequestCreateTestimonialJson testimonialDTO) {
    var graduate = this.graduateRepository.findById(graduateId)
      .orElseThrow(() -> new GraduateNotFoundException());


    var testimonial = Testimonial.builder()
    .graduate(graduate)
    .text(testimonialDTO.getText())
    .build();

    return this.testimonialRepository.save(testimonial);
  }

  public Page<Testimonial> fetchTestimonials(Integer year, int page) {
    if (page < 0) {
      page = 0;
    }

    Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "createdAt"));

    Page<Testimonial> testimonials;

    if (year == null) {
      testimonials = this.testimonialRepository.findByRegistrationStatus(StatusEnum.ACCEPTED, pageable);
    }
    else {
      testimonials = this.testimonialRepository.findByYear(year, pageable);
    }

    return testimonials;
  }

  public Page<Testimonial> fetchUnverifiedTestimonials(int page) {
    if (page < 0) {
      page = 0;
    }

    Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "createdAt"));

    Page<Testimonial> testimonials;

    testimonials = this.testimonialRepository.findByRegistrationStatus(StatusEnum.PENDING, pageable);


    return testimonials;
  }

  public Testimonial updateGraduateTestimonial(UUID testimonialId, String text) {
    Testimonial testimonial = this.testimonialRepository.findById(testimonialId)
      .orElseThrow(() -> new ResourceNotFoundException());
    
    testimonial.setText(text);
    return this.testimonialRepository.save(testimonial);
  }

  public Testimonial updateTestimonialRegisterStatus(UUID testimonialId, StatusEnum newStatus) {
    Testimonial testimonial = this.testimonialRepository.findById(testimonialId)
      .orElseThrow(() -> new ResourceNotFoundException());
    
    testimonial.setRegistrationStatus(newStatus);
    return this.testimonialRepository.save(testimonial);
  }

  public void deleteGraduateTestimonial(UUID testimonialId) {
    Testimonial testimonial = this.testimonialRepository.findById(testimonialId)
      .orElseThrow(() -> new ResourceNotFoundException());
    
    this.testimonialRepository.delete(testimonial);
  }

  public List<Testimonial> fetchGraduateTestimonials(UUID graduateId) {
    return this.testimonialRepository.findByGraduateIdOrderByCreatedAtDesc(graduateId);
  }
}
