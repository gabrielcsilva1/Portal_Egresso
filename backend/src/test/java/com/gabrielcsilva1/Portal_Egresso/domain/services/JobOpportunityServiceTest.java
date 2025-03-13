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

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Graduate;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.JobOpportunity;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.GraduateRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.JobOpportunityRepository;
import com.gabrielcsilva1.Portal_Egresso.dtos.request.jobOpportunity.RequestCreateJobOpportunityJson;
import com.gabrielcsilva1.Portal_Egresso.dtos.request.jobOpportunity.RequestUpdateJobOpportunityJson;

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
    RequestCreateJobOpportunityJson dto = RequestCreateJobOpportunityJson.builder()
    .title("Job title")
    .description("Job description")
    .build();

    var graduateMock = Graduate.builder()
      .id(UUID.randomUUID())
      .build();

    when(this.graduateRepository.findById(any(UUID.class)))
      .thenReturn(Optional.of(graduateMock));

    when(this.jobOpportunityRepository.save(any(JobOpportunity.class)))
    .thenAnswer(invocation -> invocation.getArgument(0));

    JobOpportunity result = this.sut.createJobOpportunity(graduateMock.getId(), dto);

    assertEquals(result.getTitle(), dto.getTitle());
    assertEquals(result.getDescription(), dto.getDescription());
  }

  @Test
  public void update_job_opportunity_success() {
    UUID jobOpportunityId = UUID.randomUUID();
    RequestUpdateJobOpportunityJson dto = RequestUpdateJobOpportunityJson.builder()
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

    JobOpportunity result = this.sut.updateJobOpportunity(jobOpportunityId, dto);

    assertEquals(result.getTitle(), dto.getTitle());
    assertEquals(result.getDescription(), dto.getDescription());
  }
}
