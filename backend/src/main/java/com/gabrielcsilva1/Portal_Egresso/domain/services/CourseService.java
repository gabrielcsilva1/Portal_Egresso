package com.gabrielcsilva1.Portal_Egresso.domain.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Course;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.GraduateCourse;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.CoordinatorRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.CourseRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.GraduateCourseRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.GraduateRepository;
import com.gabrielcsilva1.Portal_Egresso.dtos.request.course.RequestCreateCourseJson;
import com.gabrielcsilva1.Portal_Egresso.dtos.request.course.RequestUpdateCourseJson;
import com.gabrielcsilva1.Portal_Egresso.dtos.request.graduateCourse.RequestGraduateCourseJson;
import com.gabrielcsilva1.Portal_Egresso.exeptions.CoordinatorNotFoundException;
import com.gabrielcsilva1.Portal_Egresso.exeptions.CourseNotFoundException;
import com.gabrielcsilva1.Portal_Egresso.exeptions.GraduateAlreadyTakenTheCourseException;
import com.gabrielcsilva1.Portal_Egresso.exeptions.GraduateNotFoundException;
import com.gabrielcsilva1.Portal_Egresso.exeptions.InvalidEndYearException;
import com.gabrielcsilva1.Portal_Egresso.exeptions.core.BadRequestException;

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

  public Course createCourse(RequestCreateCourseJson courseDTO, UUID coordinatorId) {
    var coordinator = this.coordinatorRepository.findById(coordinatorId)
      .orElseThrow(() -> new CoordinatorNotFoundException());

    var course = courseDTO.toDomain();
    course.setCoordinator(coordinator);

    return this.courseRepository.save(course);
  }

  public List<Course> fetchCourses() {
    return this.courseRepository.findAllByOrderByNameAsc();
  }

  public GraduateCourse registerGraduateInCourse(RequestGraduateCourseJson graduateCourseDTO) {
    var graduate = this.graduateRepository.findById(graduateCourseDTO.getGraduateId())
      .orElseThrow(() -> new GraduateNotFoundException());

    var course = this.courseRepository.findById(graduateCourseDTO.getCourseId())
      .orElseThrow(() -> new CourseNotFoundException());

    boolean graduateAlreadyTakenTheCourse = this.graduateCourseRepository
      .findByGraduateAndCourse(graduate, course)
      .isPresent();

    if (graduateAlreadyTakenTheCourse) {
      throw new GraduateAlreadyTakenTheCourseException();
    }
    
    boolean isStartYearGreaterThanEndYear = false;

    if (graduateCourseDTO.getEndYear() != null) {
      isStartYearGreaterThanEndYear = graduateCourseDTO.getStartYear().intValue() > graduateCourseDTO.getEndYear().intValue();
    }

    if (isStartYearGreaterThanEndYear) {
      throw new InvalidEndYearException();
    }

    var graduateCourse = GraduateCourse.builder()
      .course(course)
      .graduate(graduate)
      .startYear(graduateCourseDTO.getStartYear())
      .endYear(graduateCourseDTO.getEndYear())
      .build();

    return this.graduateCourseRepository.save(graduateCourse);
  }

  public void unregisterGraduateInCourse(UUID graduateId, UUID courseId) {
    var graduate = this.graduateRepository.findById(graduateId)
      .orElseThrow(() -> new GraduateNotFoundException());

    var course = this.courseRepository.findById(courseId)
      .orElseThrow(() -> new CourseNotFoundException());

    GraduateCourse graduateCourse = this.graduateCourseRepository.findByGraduateAndCourse(graduate, course)
      .orElseThrow(() -> new BadRequestException());

    graduateCourseRepository.delete(graduateCourse);
  }

  public Course updateCourse(UUID id, RequestUpdateCourseJson courseDTO) {
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
