package com.gabrielcsilva1.Portal_Egresso.domain.services;

import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Graduate;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.GraduateRepository;
import com.gabrielcsilva1.Portal_Egresso.dtos.enums.StatusEnum;
import com.gabrielcsilva1.Portal_Egresso.dtos.request.graduate.RequestCreateGraduateJson;
import com.gabrielcsilva1.Portal_Egresso.dtos.request.graduate.RequestUpdateGraduateJson;
import com.gabrielcsilva1.Portal_Egresso.dtos.request.graduateCourse.RequestGraduateCourseJson;
import com.gabrielcsilva1.Portal_Egresso.dtos.response.authentication.ResponseGraduateAuthenticationJson;
import com.gabrielcsilva1.Portal_Egresso.exeptions.GraduateAlreadyExistsException;
import com.gabrielcsilva1.Portal_Egresso.exeptions.GraduateNotFoundException;
import com.gabrielcsilva1.Portal_Egresso.exeptions.InvalidCredentialsException;

import jakarta.transaction.Transactional;


@Service
public class GraduateService {
  @Autowired
  private GraduateRepository graduateRepository;

  @Autowired
  private CourseService courseService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private TokenService tokenService;

  @Transactional
  public Graduate createGraduate(RequestCreateGraduateJson graduateDTO) {
    var graduateWithSameEmail = this.graduateRepository.findByEmail(graduateDTO.getEmail());

    if (graduateWithSameEmail.isPresent()) {
      throw new GraduateAlreadyExistsException("email", graduateDTO.getEmail());
    }

    Graduate graduate = graduateDTO.toEntity();

    String passwordHash = this.passwordEncoder.encode(graduate.getPassword());

    graduate.setPassword(passwordHash);

    graduate = this.graduateRepository.save(graduate);

    var graduateCourseDTO = RequestGraduateCourseJson.builder()
      .graduateId(graduate.getId())
      .courseId(graduateDTO.getCourseId())
      .startYear(graduateDTO.getStartYear())
      .endYear(graduateDTO.getEndYear())
      .build();

    var graduateCourse = courseService.registerGraduateInCourse(graduateCourseDTO);

    graduate.setGraduateCourses(Set.of(graduateCourse));

    return graduate;
  }
  
  public ResponseGraduateAuthenticationJson login(String email, String password) {
    Graduate graduate = this.graduateRepository.findByEmail(email)
      .orElseThrow(() -> new InvalidCredentialsException());

    boolean isPasswordValid = passwordEncoder.matches(password, graduate.getPassword());

    if (!isPasswordValid) {
      throw new InvalidCredentialsException();
    }

    String accessToken = "";

    if (graduate.getRegistrationStatus().equals(StatusEnum.ACCEPTED)) {
      accessToken = tokenService.generateToken(graduate.getId().toString(), graduate.getRoles());
    }
    else if (graduate.getRegistrationStatus().equals(StatusEnum.REJECTED)) {
      graduateRepository.delete(graduate);
    }

    return ResponseGraduateAuthenticationJson.toResponse(accessToken, graduate);
  }

  public Graduate getGraduateById(UUID id) {
    Graduate graduate = this.graduateRepository.findById(id)
      .orElseThrow(() -> new GraduateNotFoundException());

    return graduate;
  }

  public Page<Graduate> fetchVerifiedGraduates(Specification<Graduate> queryFilters, Integer pageIndex) {
    if (pageIndex < 0) {
      pageIndex = 0;
    }
    
    Pageable pageable = PageRequest.of(pageIndex, 5);

    return this.graduateRepository.findAll(queryFilters, pageable);
  }

  public Page<Graduate> fetchUnverifiedGraduates(int pageIndex) {
    if (pageIndex < 0) {
      pageIndex = 0;
    }
    
    Pageable pageable = PageRequest.of(pageIndex, 5);

    return this.graduateRepository.findByRegistrationStatus(StatusEnum.PENDING, pageable);
  }

  public Graduate updateGraduate(UUID id, RequestUpdateGraduateJson graduateDTO) {
    var graduate = this.graduateRepository.findById(id)
      .orElseThrow( () -> new GraduateNotFoundException());

    graduate.setName(graduateDTO.getName());
    graduate.setEmail(graduateDTO.getEmail());
    graduate.setDescription(graduateDTO.getDescription());
    graduate.setAvatarUrl(graduateDTO.getAvatarUrl());
    graduate.setLinkedin(graduateDTO.getLinkedin());
    graduate.setInstagram(graduateDTO.getInstagram());
    graduate.setCurriculum(graduateDTO.getCurriculum());

    return this.graduateRepository.save(graduate);
  }

  public Graduate updateGraduateRegisterStatus(UUID id, StatusEnum newStatus) {
    var graduate = this.graduateRepository.findById(id)
      .orElseThrow( () -> new GraduateNotFoundException());

    graduate.setRegistrationStatus(newStatus);

    return this.graduateRepository.save(graduate);
  }

  public void deleteGraduate(UUID id) {
    var graduate = this.graduateRepository.findById(id)
      .orElseThrow(() -> new GraduateNotFoundException());

    this.graduateRepository.delete(graduate);
  }
}
