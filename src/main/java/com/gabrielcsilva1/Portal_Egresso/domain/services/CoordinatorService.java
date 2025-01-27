package com.gabrielcsilva1.Portal_Egresso.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.CoordinatorDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Coordinator;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.CoordinatorRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.CoordinatorAlreadyExistsException;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.InvalidCredentialsException;

@Service
public class CoordinatorService{
  @Autowired
  private CoordinatorRepository coordinatorRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private TokenService tokenService;

  public String authenticate(String login, String password) {
    Coordinator coordinator = this.coordinatorRepository.findByLogin(login)
      .orElseThrow(() -> new InvalidCredentialsException());

    boolean isPasswordValid = passwordEncoder.matches(password, coordinator.getPassword());

    if (!isPasswordValid) {
      throw new InvalidCredentialsException();
    }

    String accessToken = tokenService.generateToken(coordinator.getId().toString());

    return accessToken;
  }

  public Coordinator create(CoordinatorDTO coordinatorDTO) {
    Coordinator coordinator = coordinatorDTO.toEntity();

    boolean coordinatorAlreadyExists = coordinatorRepository.findByLogin(coordinatorDTO.getLogin())
      .isPresent();

    if (coordinatorAlreadyExists) {
      throw new CoordinatorAlreadyExistsException();
    }

    String passwordHash = passwordEncoder.encode(coordinatorDTO.getPassword());
    coordinator.setPassword(passwordHash);

    return coordinatorRepository.save(coordinator);
  }
}
