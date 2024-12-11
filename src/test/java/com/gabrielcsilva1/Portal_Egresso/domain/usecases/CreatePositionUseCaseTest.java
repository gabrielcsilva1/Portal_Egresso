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

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.PositionDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Egress;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Position;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.EgressRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.PositionRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.usecases.exeptions.EgressNotFoundException;

@ExtendWith(MockitoExtension.class)
public class CreatePositionUseCaseTest {
  @InjectMocks
  private CreatePositionUseCase sut;

  @Mock
  private PositionRepository positionRepository;

  @Mock
  private EgressRepository egressRepository;

  @Test
  @DisplayName("should be able to create a new position")
  public void success() {
    // DTO
    PositionDTO positionDTO = PositionDTO.builder()
      .egressId(UUID.randomUUID())
      .description("Position Description")
      .location("Location Name")
      .startYear(2000)
      .endYear(2020)
      .build();

    // Mocks
    Egress egressMock = Egress.builder()
      .id(positionDTO.getEgressId())
      .build();
    
    Position positionMock = Position.builder()
      .id(UUID.randomUUID())
      .egress(egressMock)
      .description(positionDTO.getDescription())
      .location(positionDTO.getLocation())
      .startYear(positionDTO.getStartYear())
      .endYear(positionDTO.getEndYear())
      .build();

    when(this.egressRepository.findById(positionDTO.getEgressId()))
      .thenReturn(Optional.of(egressMock));

    when(this.positionRepository.save(any(Position.class)))
      .thenReturn(positionMock);

    // Test
    Position result = this.sut.execute(positionDTO);

    assertEquals(result.getId(), positionMock.getId());
    assertEquals(result.getEgress().getId(), positionDTO.getEgressId());
    assertEquals(result.getDescription(), positionDTO.getDescription());
  }

  @Test
  @DisplayName("should not be able to create a new position if the egress does not exist")
  public void egressNotFound() {
    // DTO
    PositionDTO positionDTO = PositionDTO.builder()
      .egressId(UUID.randomUUID())
      .description("Position Description")
      .location("Location Name")
      .startYear(2000)
      .endYear(2020)
      .build();

    // Mocks
    when(this.egressRepository.findById(positionDTO.getEgressId()))
      .thenReturn(Optional.empty());

    // Test
    assertThrows(EgressNotFoundException.class, () -> {
      this.sut.execute(positionDTO);
    });
  }
}
