package com.gabrielcsilva1.Portal_Egresso.domain.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.JobOpportunityDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.jobOpportunity.UpdateJobOpportunityDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Graduate;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.JobOpportunity;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.GraduateRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.JobOpportunityRepository;

@ExtendWith(MockitoExtension.class)
public class JobOpportunityServiceTest {
  @InjectMocks
  private JobOpportunityService sut;

  @Mock
  private JobOpportunityRepository jobOpportunityRepository;

  @Mock
  private GraduateRepository graduateRepository;

  @Test
  public void create_job_opportunity_success() {
    JobOpportunityDTO dto = JobOpportunityDTO.builder()
    .graduateId(UUID.randomUUID())
    .title("Job title")
    .description("Job description")
    .build();

    var graduateMock = Graduate.builder()
      .id(dto.getGraduateId())
      .build();

    when(this.graduateRepository.findById(any(UUID.class)))
      .thenReturn(Optional.of(graduateMock));

    when(this.jobOpportunityRepository.save(any(JobOpportunity.class)))
    .thenAnswer(invocation -> invocation.getArgument(0));

    JobOpportunity result = this.sut.create(dto);

    assertEquals(result.getTitle(), dto.getTitle());
    assertEquals(result.getDescription(), dto.getDescription());
  }

  @Test
  public void update_job_opportunity_success() {
    UUID jobOpportunityId = UUID.randomUUID();
    UpdateJobOpportunityDTO dto = UpdateJobOpportunityDTO.builder()
    .title("New Job title")
    .description("New Job description")
    .build();

    var jobOpportunityMock = JobOpportunity.builder()
    .id(jobOpportunityId)
    .title("Old Job title")
    .description("Old Job description")
    .build();

    when(this.jobOpportunityRepository.findById(any(UUID.class)))
      .thenReturn(Optional.of(jobOpportunityMock));

    when(this.jobOpportunityRepository.save(any(JobOpportunity.class)))
    .thenAnswer(invocation -> invocation.getArgument(0));

    JobOpportunity result = this.sut.update(jobOpportunityId, dto);

    assertEquals(result.getTitle(), dto.getTitle());
    assertEquals(result.getDescription(), dto.getDescription());
  }
}
