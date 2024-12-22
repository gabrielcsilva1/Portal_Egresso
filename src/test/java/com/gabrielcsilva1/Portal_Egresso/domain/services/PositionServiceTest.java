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
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Graduate;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Position;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.GraduateRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.PositionRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.GraduateNotFoundException;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.InvalidEndYearException;

@ExtendWith(MockitoExtension.class)
public class PositionServiceTest {
  @InjectMocks
  private PositionService sut;

  @Mock
  private PositionRepository positionRepository;

  @Mock
  private GraduateRepository graduateRepository;
  
  @Test
  @DisplayName("should be able to create a new position")
  public void register_graduate_success() {
    // DTO
    PositionDTO positionDTO = PositionDTO.builder()
      .graduateId(UUID.randomUUID())
      .description("Position Description")
      .location("Location Name")
      .startYear(2000)
      .endYear(2020)
      .build();

    // Mocks
    Graduate graduateMock = Graduate.builder()
      .id(positionDTO.getGraduateId())
      .build();
    
    Position positionMock = Position.builder()
      .id(UUID.randomUUID())
      .graduate(graduateMock)
      .description(positionDTO.getDescription())
      .location(positionDTO.getLocation())
      .startYear(positionDTO.getStartYear())
      .endYear(positionDTO.getEndYear())
      .build();

    when(this.graduateRepository.findById(positionDTO.getGraduateId()))
      .thenReturn(Optional.of(graduateMock));

    when(this.positionRepository.save(any(Position.class)))
      .thenReturn(positionMock);

    // Test
    Position result = this.sut.registerGraduatePosition(positionDTO);

    assertEquals(result.getId(), positionMock.getId());
    assertEquals(result.getGraduate().getId(), positionDTO.getGraduateId());
    assertEquals(result.getDescription(), positionDTO.getDescription());
  }

  @Test
  @DisplayName("should not be able to create a new position if the graduate does not exist")
  public void register_graduate_position_graduate_not_found() {
    // DTO
    PositionDTO positionDTO = PositionDTO.builder()
      .graduateId(UUID.randomUUID())
      .description("Position Description")
      .location("Location Name")
      .startYear(2000)
      .endYear(2020)
      .build();

    // Mocks
    when(this.graduateRepository.findById(positionDTO.getGraduateId()))
      .thenReturn(Optional.empty());

    // Test
    assertThrows(GraduateNotFoundException.class, () -> {
      sut.registerGraduatePosition(positionDTO);
    });
  }

  @Test
  @DisplayName("should not be able to create a new position with an end year before the start year")
  public void register_graduate_position_invalid_end_year() {
    // DTO
    PositionDTO positionDTO = PositionDTO.builder()
      .graduateId(UUID.randomUUID())
      .description("Position Description")
      .location("Location Name")
      .startYear(2020)
      .endYear(2000)
      .build();

    // Mocks
    Graduate graduateMock = new Graduate();

    when(this.graduateRepository.findById(positionDTO.getGraduateId()))
      .thenReturn(Optional.of(graduateMock));

    // Test
    assertThrows(InvalidEndYearException.class, () -> {
      sut.registerGraduatePosition(positionDTO);
    });
  }

  @Test
  @DisplayName("should be able to update the graduate position")
  public void update_graduate_position_success() {
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

    Position result = sut.updateGraduatePosition(positionMock.getId(), positionDTO);

    assertNotNull(result);
    assertEquals(result.getDescription(), positionDTO.getDescription());
    assertEquals(result.getLocation(), positionDTO.getLocation());
    assertEquals(result.getStartYear(), positionDTO.getStartYear());
    assertEquals(result.getEndYear(), positionDTO.getEndYear());
  }

}
