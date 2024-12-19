package com.gabrielcsilva1.Portal_Egresso.domain.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.position.UpdatePositionDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Egress;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Position;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.EgressRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.PositionRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.EgressNotFoundException;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.InvalidEndYearException;

@ExtendWith(MockitoExtension.class)
public class PositionServiceTest {
  @InjectMocks
  private PositionService sut;

  @Mock
  private PositionRepository positionRepository;

  @Mock
  private EgressRepository egressRepository;
  
  @Test
  @DisplayName("should be able to create a new position")
  public void register_egress_success() {
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
    Position result = this.sut.registerEgressPosition(positionDTO);

    assertEquals(result.getId(), positionMock.getId());
    assertEquals(result.getEgress().getId(), positionDTO.getEgressId());
    assertEquals(result.getDescription(), positionDTO.getDescription());
  }

  @Test
  @DisplayName("should not be able to create a new position if the egress does not exist")
  public void register_egress_position_egress_not_found() {
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
      sut.registerEgressPosition(positionDTO);
    });
  }

  @Test
  @DisplayName("should not be able to create a new position with an end year before the start year")
  public void register_egress_position_invalid_end_year() {
    // DTO
    PositionDTO positionDTO = PositionDTO.builder()
      .egressId(UUID.randomUUID())
      .description("Position Description")
      .location("Location Name")
      .startYear(2020)
      .endYear(2000)
      .build();

    // Mocks
    Egress egressMock = new Egress();

    when(this.egressRepository.findById(positionDTO.getEgressId()))
      .thenReturn(Optional.of(egressMock));

    // Test
    assertThrows(InvalidEndYearException.class, () -> {
      sut.registerEgressPosition(positionDTO);
    });
  }

  @Test
  @DisplayName("should be able to update the egress position")
  public void update_egress_position_success() {
    UpdatePositionDTO positionDTO = UpdatePositionDTO.builder()
      .description("new description")
      .location("new location")
      .startYear(2010)
      .endYear(2020)
      .build();

    Position positionMock = Position.builder()
      .id(UUID.randomUUID())
      .build();

    when(positionRepository.findById(any(UUID.class)))
      .thenReturn(Optional.of(positionMock));
    when(positionRepository.save(any(Position.class)))
      .thenAnswer(invocation -> invocation.getArgument(0));

    Position result = sut.updateEgressPosition(positionMock.getId(), positionDTO);

    assertNotNull(result);
    assertEquals(result.getDescription(), positionDTO.getDescription());
    assertEquals(result.getLocation(), positionDTO.getLocation());
    assertEquals(result.getStartYear(), positionDTO.getStartYear());
    assertEquals(result.getEndYear(), positionDTO.getEndYear());
  }

}
