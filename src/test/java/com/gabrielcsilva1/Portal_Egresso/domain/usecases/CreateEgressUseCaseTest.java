package com.gabrielcsilva1.Portal_Egresso.domain.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.EgressDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Egress;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.EgressRepository;
import com.gabrielcsilva1.Portal_Egresso.exeptions.EgressAlreadyExistsException;

public class CreateEgressUseCaseTest {
  @InjectMocks
  private CreateEgressUseCase createEgressUseCase;

  @Mock
  private EgressRepository egressRepository;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("should be able to create a new egress.")
  public void success() {
    EgressDTO egressDTO = EgressDTO.builder()
      .email("johnDoe@example.com")
      .name("John Doe")
      .build();
    
    Egress mockEgress = Egress.builder()
      .id(UUID.randomUUID())
      .name(egressDTO.getName())
      .email(egressDTO.getEmail())
      .build();

    when(egressRepository.findByEmail(egressDTO.getEmail()))
      .thenReturn(Optional.empty());
    
    when(egressRepository.save(any(Egress.class)))
      .thenReturn(mockEgress);

    var result = this.createEgressUseCase.execute(egressDTO);

    assertEquals(result.getId(), mockEgress.getId());
    assertEquals(result.getEmail(), mockEgress.getEmail());
    assertEquals(result.getName(), mockEgress.getName());
  }

  @Test
  @DisplayName("should not be able to create a new egress with a duplicate email address.")
  public void emailAlreadyExists() {
    EgressDTO egressDTO = EgressDTO.builder()
      .email("johnDoe@example.com")
      .name("John Doe")
      .build();
    
    Egress mockEgress = Egress.builder()
      .id(UUID.randomUUID())
      .name(egressDTO.getName())
      .email(egressDTO.getEmail())
      .build();

    when(egressRepository.findByEmail(egressDTO.getEmail()))
      .thenReturn(Optional.of(mockEgress));
    

    assertThrows(EgressAlreadyExistsException.class, () -> {
      this.createEgressUseCase.execute(egressDTO);
    });
  }
}
