package com.gabrielcsilva1.Portal_Egresso.domain.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.CourseDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Course;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.CoordinatorRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.CourseRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.usecases.exeptions.CoordinatorNotFoundException;

@Service
public class CreateCourseUseCase {
  @Autowired
  private CourseRepository courseRepository;

  @Autowired
  private CoordinatorRepository coordinatorRepository;

  public Course execute(CourseDTO courseDTO) {
    var coordinator = this.coordinatorRepository.findById(courseDTO.getCoordinatorId());

    if (coordinator.isEmpty()) {
      throw new CoordinatorNotFoundException();
    }

    var course = Course.builder()
      .coordinator(coordinator.get())
      .name(courseDTO.getName())
      .level(courseDTO.getLevel())
      .build();

    return this.courseRepository.save(course);
  }
}
