package com.gabrielcsilva1.Portal_Egresso.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gabrielcsilva1.Portal_Egresso.domain.cryptography.IPasswordHasher;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Coordinator;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.CoordinatorRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.InvalidCredentialsException;

@Service
public class CoordinatorService {
  @Autowired
  private CoordinatorRepository coordinatorRepository;

  @Autowired
  private IPasswordHasher passwordHasher;

  public Coordinator login(String login, String password) {
    var coordinator = this.coordinatorRepository.findByLogin(login)
      .orElseThrow(() -> new InvalidCredentialsException());

    boolean isInvalidPassword = !this.passwordHasher.verify(password, coordinator.getPassword());

    if (isInvalidPassword) {
      throw new InvalidCredentialsException();
    }
    return coordinator;
  }
}
