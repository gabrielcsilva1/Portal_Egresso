package com.gabrielcsilva1.Portal_Egresso.seeder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Coordinator;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.CoordinatorRepository;

@Component
public class DatabaseSeeder implements CommandLineRunner {

  @Autowired
  private CoordinatorRepository coordinatorRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public void run(String... args) throws Exception {
    if (coordinatorRepository.count() == 0) {
      Coordinator coordinator1 = Coordinator.builder()
        .login("admin")
        .password(this.passwordEncoder.encode("admin"))
        .build();

      Coordinator coordinator2 = Coordinator.builder()
        .login("user")
        .password(this.passwordEncoder.encode("123456"))
        .build();

      this.coordinatorRepository.save(coordinator1);
      this.coordinatorRepository.save(coordinator2);
      System.out.println("Dados de seed criados!");
    }
  }
}
