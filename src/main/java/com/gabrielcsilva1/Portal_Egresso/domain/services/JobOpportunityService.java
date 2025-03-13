package com.gabrielcsilva1.Portal_Egresso.domain.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Graduate;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.JobOpportunity;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.GraduateRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.JobOpportunityRepository;
import com.gabrielcsilva1.Portal_Egresso.dtos.enums.StatusEnum;
import com.gabrielcsilva1.Portal_Egresso.dtos.request.jobOpportunity.RequestCreateJobOpportunityJson;
import com.gabrielcsilva1.Portal_Egresso.dtos.request.jobOpportunity.RequestUpdateJobOpportunityJson;
import com.gabrielcsilva1.Portal_Egresso.exeptions.GraduateNotFoundException;
import com.gabrielcsilva1.Portal_Egresso.exeptions.ResourceNotFoundException;

@Service
public class JobOpportunityService {
  @Autowired
  private JobOpportunityRepository jobOpportunityRepository;

  @Autowired
  private GraduateRepository graduateRepository;

  public JobOpportunity createJobOpportunity(UUID graduateId, RequestCreateJobOpportunityJson jobOpportunityDTO) {
    Graduate graduate = graduateRepository.findById(graduateId)
      .orElseThrow(() -> new GraduateNotFoundException());

    var jobOpportunity = JobOpportunity.builder()
      .graduate(graduate)
      .title(jobOpportunityDTO.getTitle())
      .description(jobOpportunityDTO.getDescription())
      .build();

    return jobOpportunityRepository.save(jobOpportunity);
  }

  public JobOpportunity updateJobOpportunity(UUID id, RequestUpdateJobOpportunityJson jobOpportunityDTO) {
    JobOpportunity jobOpportunity = jobOpportunityRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException());
    
    jobOpportunity.setTitle(jobOpportunityDTO.getTitle());
    jobOpportunity.setDescription(jobOpportunityDTO.getDescription());

    return jobOpportunityRepository.save(jobOpportunity);
  }

  public JobOpportunity updateJobOpportunityRegisterStatus(UUID id, StatusEnum newStatus) {
    JobOpportunity jobOpportunity = jobOpportunityRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException());
    
    jobOpportunity.setRegistrationStatus(newStatus);

    return jobOpportunityRepository.save(jobOpportunity);
  }

  public Page<JobOpportunity> fetchVerifiedJobOpportunities(int pageIndex) {
    if (pageIndex < 0) {
      pageIndex = 0;
    }
    Pageable pageable = PageRequest.of(pageIndex, 5, Sort.by(Sort.Direction.DESC, "createdAt"));

    return jobOpportunityRepository.findByRegistrationStatus(StatusEnum.ACCEPTED, pageable);
  }

  public Page<JobOpportunity> fetchUnverifiedJobOpportunities(int pageIndex) {
    if (pageIndex < 0) {
      pageIndex = 0;
    }
    Pageable pageable = PageRequest.of(pageIndex, 5, Sort.by(Sort.Direction.DESC, "createdAt"));

    return jobOpportunityRepository.findByRegistrationStatus(StatusEnum.PENDING, pageable);
  }

  

  public void deleteJobOpportunity(UUID jobId) {
    JobOpportunity jobOpportunity = jobOpportunityRepository.findById(jobId)
     .orElseThrow(() -> new ResourceNotFoundException());

    jobOpportunityRepository.delete(jobOpportunity);
  }

  public List<JobOpportunity> fetchGraduateJobOpportunity(UUID graduateId) {
    return this.jobOpportunityRepository.findByGraduateIdOrderByCreatedAtDesc(graduateId);
  }
}
