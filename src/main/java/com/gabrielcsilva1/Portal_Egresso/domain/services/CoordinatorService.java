package com.gabrielcsilva1.Portal_Egresso.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Coordinator;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.CoordinatorRepository;
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
}
