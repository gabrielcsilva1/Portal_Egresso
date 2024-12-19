package com.gabrielcsilva1.Portal_Egresso.domain.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.CourseDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.EgressCourseDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.course.UpdateCourseDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Course;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.EgressCourse;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.CoordinatorRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.CourseRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.EgressCourseRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.EgressRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.ResourceNotFoundException;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.CoordinatorNotFoundException;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.CourseNotFoundException;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.EgressAlreadyTakenTheCourseException;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.EgressNotFoundException;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.InvalidEndYearException;

@Service
public class CourseService {
  @Autowired
  private CourseRepository courseRepository;

  @Autowired
  private CoordinatorRepository coordinatorRepository;

  @Autowired
  private EgressRepository egressRepository;

  @Autowired
  private EgressCourseRepository egressCourseRepository;

  public Course createCourse(CourseDTO courseDTO) {
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

  public List<Course> fetchCourses() {
    return this.courseRepository.findAllByOrderByNameAsc();
  }

  public EgressCourse registerEgressInCourse(EgressCourseDTO egressCourseDTO) {
    var egress = this.egressRepository.findById(egressCourseDTO.getEgressId());

    if (egress.isEmpty()) {
      throw new EgressNotFoundException();
    }

    var course = this.courseRepository.findById(egressCourseDTO.getCourseId());

    if (course.isEmpty()) {
      throw new CourseNotFoundException();
    }

    boolean egressAlreadyTakenTheCourse = this.egressCourseRepository
      .findByEgressAndCourse(egress.get(), course.get())
      .isPresent();

    if (egressAlreadyTakenTheCourse) {
      throw new EgressAlreadyTakenTheCourseException(course.get().getName());
    }
    
    boolean isStartYearGreaterThanEndYear = false;

    if (egressCourseDTO.getEndYear() != null) {
      isStartYearGreaterThanEndYear = egressCourseDTO.getStartYear().intValue() > egressCourseDTO.getEndYear().intValue();
    }

    if (isStartYearGreaterThanEndYear) {
      throw new InvalidEndYearException();
    }

    var egressCourse = EgressCourse.builder()
      .course(course.get())
      .egress(egress.get())
      .startYear(egressCourseDTO.getStartYear())
      .endYear(egressCourseDTO.getEndYear())
      .build();

    return this.egressCourseRepository.save(egressCourse);
  }

  public void unregisterEgressInCourse(UUID id) {
    EgressCourse egressCourse = egressCourseRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException());

    egressCourseRepository.delete(egressCourse);
  }

  public Course updateCourse(UUID id, UpdateCourseDTO courseDTO) {
    Course course = this.courseRepository.findById(id)
      .orElseThrow(() -> new CourseNotFoundException());

    course.setName(courseDTO.getName());
    course.setLevel(courseDTO.getLevel());

    return this.courseRepository.save(course);
  }

  public void deleteCourse(UUID id) {
    Course course = this.courseRepository.findById(id)
      .orElseThrow(() -> new CourseNotFoundException());

    this.courseRepository.delete(course);
  }
}
