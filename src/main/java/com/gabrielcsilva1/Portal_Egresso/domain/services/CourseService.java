package com.gabrielcsilva1.Portal_Egresso.domain.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.CourseDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.GraduateCourseDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.course.UpdateCourseDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Course;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.GraduateCourse;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.CoordinatorRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.CourseRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.GraduateCourseRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.GraduateRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.ResourceNotFoundException;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.CoordinatorNotFoundException;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.CourseNotFoundException;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.GraduateAlreadyTakenTheCourseException;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.GraduateNotFoundException;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.InvalidEndYearException;

@Service
public class CourseService {
  @Autowired
  private CourseRepository courseRepository;

  @Autowired
  private CoordinatorRepository coordinatorRepository;

  @Autowired
  private GraduateRepository graduateRepository;

  @Autowired
  private GraduateCourseRepository graduateCourseRepository;

  public Course createCourse(CourseDTO courseDTO, UUID coordinatorId) {
    var coordinator = this.coordinatorRepository.findById(coordinatorId);

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

  public GraduateCourse registerGraduateInCourse(GraduateCourseDTO graduateCourseDTO) {
    var graduate = this.graduateRepository.findById(graduateCourseDTO.getGraduateId());

    if (graduate.isEmpty()) {
      throw new GraduateNotFoundException();
    }

    var course = this.courseRepository.findById(graduateCourseDTO.getCourseId());

    if (course.isEmpty()) {
      throw new CourseNotFoundException();
    }

    boolean graduateAlreadyTakenTheCourse = this.graduateCourseRepository
      .findByGraduateAndCourse(graduate.get(), course.get())
      .isPresent();

    if (graduateAlreadyTakenTheCourse) {
      throw new GraduateAlreadyTakenTheCourseException(course.get().getName());
    }
    
    boolean isStartYearGreaterThanEndYear = false;

    if (graduateCourseDTO.getEndYear() != null) {
      isStartYearGreaterThanEndYear = graduateCourseDTO.getStartYear().intValue() > graduateCourseDTO.getEndYear().intValue();
    }

    if (isStartYearGreaterThanEndYear) {
      throw new InvalidEndYearException();
    }

    var graduateCourse = GraduateCourse.builder()
      .course(course.get())
      .graduate(graduate.get())
      .startYear(graduateCourseDTO.getStartYear())
      .endYear(graduateCourseDTO.getEndYear())
      .build();

    return this.graduateCourseRepository.save(graduateCourse);
  }

  public void unregisterGraduateInCourse(UUID id) {
    GraduateCourse graduateCourse = graduateCourseRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException());

    graduateCourseRepository.delete(graduateCourse);
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
