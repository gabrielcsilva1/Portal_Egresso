package com.gabrielcsilva1.Portal_Egresso.domain.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.JobOpportunityDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.jobOpportunity.UpdateJobOpportunityDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Graduate;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.JobOpportunity;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.GraduateRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.JobOpportunityRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.GraduateNotFoundException;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.ResourceNotFoundException;

@Service
public class JobOpportunityService {
  @Autowired
  private JobOpportunityRepository jobOpportunityRepository;

  @Autowired
  private GraduateRepository graduateRepository;

  public JobOpportunity create(JobOpportunityDTO jobOpportunityDTO) {
    Graduate graduate = graduateRepository.findById(jobOpportunityDTO.getGraduateId())
      .orElseThrow(() -> new GraduateNotFoundException());

    var jobOpportunity = JobOpportunity.builder()
      .graduate(graduate)
      .title(jobOpportunityDTO.getTitle())
      .description(jobOpportunityDTO.getDescription())
      .build();

    return jobOpportunityRepository.save(jobOpportunity);
  }

  public JobOpportunity update(UUID id, UpdateJobOpportunityDTO jobOpportunityDTO) {
    JobOpportunity jobOpportunity = jobOpportunityRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException());
    
    jobOpportunity.setTitle(jobOpportunityDTO.getTitle());
    jobOpportunity.setDescription(jobOpportunityDTO.getDescription());
    jobOpportunity.setIsVerified(false);

    return jobOpportunityRepository.save(jobOpportunity);
  }

  public List<JobOpportunity> fetchVerifiedJobOpportunities() {
    return jobOpportunityRepository.findByIsVerifiedTrue();
  }

  public List<JobOpportunity> fetchUnverifiedJobOpportunities() {
    return jobOpportunityRepository.findByIsVerifiedFalse();
  }

  public void delete(UUID jobId) {
    JobOpportunity jobOpportunity = jobOpportunityRepository.findById(jobId)
     .orElseThrow(() -> new ResourceNotFoundException());

    jobOpportunityRepository.delete(jobOpportunity);
  }
}
