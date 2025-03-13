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

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Graduate;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Position;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.GraduateRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.PositionRepository;
import com.gabrielcsilva1.Portal_Egresso.dtos.request.position.RequestCreatePositionJson;
import com.gabrielcsilva1.Portal_Egresso.dtos.request.position.RequestUpdatePositionJson;
import com.gabrielcsilva1.Portal_Egresso.exeptions.GraduateNotFoundException;
import com.gabrielcsilva1.Portal_Egresso.exeptions.InvalidEndYearException;
import com.gabrielcsilva1.utils.faker.FakeGraduateFactory;

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
    UUID graduateID = UUID.randomUUID();
    // DTO
    RequestCreatePositionJson positionDTO = RequestCreatePositionJson.builder()
      .description("Position Description")
      .location("Location Name")
      .startYear(2000)
      .endYear(2020)
      .build();

    // Mocks
    Graduate graduateMock = Graduate.builder()
      .id(graduateID)
      .build();
    
    Position positionMock = Position.builder()
      .id(UUID.randomUUID())
      .graduate(graduateMock)
      .description(positionDTO.getDescription())
      .location(positionDTO.getLocation())
      .startYear(positionDTO.getStartYear())
      .endYear(positionDTO.getEndYear())
      .build();

    when(this.graduateRepository.findById(graduateID))
      .thenReturn(Optional.of(graduateMock));

    when(this.positionRepository.save(any(Position.class)))
      .thenReturn(positionMock);

    // Test
    Position result = this.sut.registerGraduatePosition(graduateID, positionDTO);

    assertEquals(result.getId(), positionMock.getId());
    assertEquals(result.getGraduate().getId(), graduateID);
    assertEquals(result.getDescription(), positionDTO.getDescription());
  }

  @Test
  @DisplayName("should not be able to create a new position if the graduate does not exist")
  public void register_graduate_position_graduate_not_found() {
    UUID graduateID = UUID.randomUUID();

    // DTO
    RequestCreatePositionJson positionDTO = RequestCreatePositionJson.builder()
      .description("Position Description")
      .location("Location Name")
      .startYear(2000)
      .endYear(2020)
      .build();

    // Mocks
    when(this.graduateRepository.findById(graduateID))
      .thenReturn(Optional.empty());

    // Test
    assertThrows(GraduateNotFoundException.class, () -> {
      sut.registerGraduatePosition(graduateID, positionDTO);
    });
  }

  @Test
  @DisplayName("should not be able to create a new position with an end year before the start year")
  public void register_graduate_position_invalid_end_year() {
    // DTO
    RequestCreatePositionJson positionDTO = RequestCreatePositionJson.builder()
      .description("Position Description")
      .location("Location Name")
      .startYear(2020)
      .endYear(2000)
      .build();

    // Mocks
    Graduate graduateMock = new Graduate();

    when(this.graduateRepository.findById(any(UUID.class)))
      .thenReturn(Optional.of(graduateMock));

    // Test
    assertThrows(InvalidEndYearException.class, () -> {
      sut.registerGraduatePosition(UUID.randomUUID(), positionDTO);
    });
  }

  @Test
  @DisplayName("should be able to update the graduate position")
  public void update_graduate_position_success() {
    RequestUpdatePositionJson positionDTO = RequestUpdatePositionJson.builder()
      .description("new description")
      .location("new location")
      .startYear(2010)
      .endYear(2020)
      .build();

    Graduate graduateMock = FakeGraduateFactory.makeGraduate();
    graduateMock.setId(UUID.randomUUID());

    Position positionMock = Position.builder()
      .id(UUID.randomUUID())
      .graduate(graduateMock)
      .build();

    when(positionRepository.findById(any(UUID.class)))
      .thenReturn(Optional.of(positionMock));
    when(positionRepository.save(any(Position.class)))
      .thenAnswer(invocation -> invocation.getArgument(0));

    Position result = sut.updateGraduatePosition(positionMock.getId(), graduateMock.getId(), positionDTO);

    assertNotNull(result);
    assertEquals(result.getDescription(), positionDTO.getDescription());
    assertEquals(result.getLocation(), positionDTO.getLocation());
    assertEquals(result.getStartYear(), positionDTO.getStartYear());
    assertEquals(result.getEndYear(), positionDTO.getEndYear());
  }

}
