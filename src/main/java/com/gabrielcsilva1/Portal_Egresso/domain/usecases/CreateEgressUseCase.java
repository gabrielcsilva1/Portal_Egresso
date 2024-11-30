package com.gabrielcsilva1.Portal_Egresso.domain.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.EgressDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Egress;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.EgressRepository;
import com.gabrielcsilva1.Portal_Egresso.exeptions.EgressAlreadyExistsException;

@Service
public class CreateEgressUseCase {
  @Autowired
  private EgressRepository egressRepository;

  public Egress execute(EgressDTO egressDTO) {
    var egressWithSameEmail = this.egressRepository.findByEmail(egressDTO.getEmail());

    if (egressWithSameEmail.isPresent()) {
      throw new EgressAlreadyExistsException("email", egressDTO.getEmail());
    }

    Egress egress = new Egress(egressDTO);

    return this.egressRepository.save(egress);
  }
}
