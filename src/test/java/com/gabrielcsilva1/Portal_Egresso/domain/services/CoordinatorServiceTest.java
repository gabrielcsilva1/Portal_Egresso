package com.gabrielcsilva1.Portal_Egresso.domain.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gabrielcsilva1.Portal_Egresso.domain.cryptography.IPasswordHasher;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Coordinator;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.CoordinatorRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.InvalidCredentialsException;

@ExtendWith(MockitoExtension.class)
public class CoordinatorServiceTest {
  @InjectMocks
  private CoordinatorService sut;

  @Mock
  private CoordinatorRepository coordinatorRepository;

  @Mock
  private IPasswordHasher passwordHasher;

  @Test
  @DisplayName("should be able to perform coordinator login")
  public void login_success() {
    String login = "jonhdoe@example.com";
    String password = "secret";
    String passwordHash = "secret-hasher";

    // Mocks
    Coordinator coordinatorMock = Coordinator.builder()
      .id(UUID.randomUUID())
      .login(login)
      .password(passwordHash)
      .build();

    when(coordinatorRepository.findByLogin(login))
      .thenReturn(Optional.of(coordinatorMock));

    when(passwordHasher.verify(password, passwordHash))
      .thenReturn(true);

    // Test
    Coordinator result = sut.login(login, password);

    assertEquals(result.getId(), coordinatorMock.getId());
    assertEquals(result.getLogin(), coordinatorMock.getLogin());
  }

  @Test
  @DisplayName("should not be able to perform coordinator login with wrong login")
  public void login_invalid_login() {
    String login = "jonhdoe@example.com";
    String password = "secret";

    // Mocks
    when(coordinatorRepository.findByLogin(login))
      .thenReturn(Optional.empty());

    // Test
    assertThrows(InvalidCredentialsException.class, () -> {
      sut.login(login, password);
    });
  }

  @Test
  @DisplayName("should not be able to perform coordinator login with wrong password")
  public void login_invalid_password() {
    String login = "jonhdoe@example.com";
    String password = "secret";
    String passwordHash = "secret-hasher";

    // Mocks
    Coordinator coordinatorMock = Coordinator.builder()
      .id(UUID.randomUUID())
      .login(login)
      .password(passwordHash)
      .build();

    when(coordinatorRepository.findByLogin(login))
      .thenReturn(Optional.of(coordinatorMock));

    when(passwordHasher.verify(password, passwordHash))
      .thenReturn(false);

    assertThrows(InvalidCredentialsException.class, () -> {
      sut.login(login, password);
    });
  }
}
