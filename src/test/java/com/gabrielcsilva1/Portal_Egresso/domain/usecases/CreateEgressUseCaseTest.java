package com.gabrielcsilva1.Portal_Egresso.domain.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.EgressDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Egress;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.EgressRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.usecases.exeptions.EgressAlreadyExistsException;

@ExtendWith(MockitoExtension.class)
public class CreateEgressUseCaseTest {
  @InjectMocks
  private CreateEgressUseCase sut;

  @Mock
  private EgressRepository egressRepository;

  @Test
  @DisplayName("should be able to create a new egress.")
  public void success() {
    // DTO
    EgressDTO egressDTO = EgressDTO.builder()
      .email("johnDoe@example.com")
      .name("John Doe")
      .build();
    
    // Mocks
    Egress mockEgress = Egress.builder()
      .id(UUID.randomUUID())
      .name(egressDTO.getName())
      .email(egressDTO.getEmail())
      .build();

    when(egressRepository.findByEmail(egressDTO.getEmail()))
      .thenReturn(Optional.empty());
    
    when(egressRepository.save(any(Egress.class)))
      .thenReturn(mockEgress);

    // Test
    Egress result = this.sut.execute(egressDTO);

    assertEquals(result.getId(), mockEgress.getId());
    assertEquals(result.getEmail(), egressDTO.getEmail());
    assertEquals(result.getName(), egressDTO.getName());
  }

  @Test
  @DisplayName("should not be able to create a new egress with a duplicate email address.")
  public void emailAlreadyExists() {
    // DTO
    EgressDTO egressDTO = EgressDTO.builder()
      .email("johnDoe@example.com")
      .name("John Doe")
      .build();
    
    // Mocks
    Egress mockEgress = Egress.builder()
      .id(UUID.randomUUID())
      .name(egressDTO.getName())
      .email(egressDTO.getEmail())
      .build();

    when(egressRepository.findByEmail(egressDTO.getEmail()))
      .thenReturn(Optional.of(mockEgress));
    
    // Test
    assertThrows(EgressAlreadyExistsException.class, () -> {
      this.sut.execute(egressDTO);
    });
  }
}
