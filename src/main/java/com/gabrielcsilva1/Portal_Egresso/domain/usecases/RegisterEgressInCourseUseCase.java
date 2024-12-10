package com.gabrielcsilva1.Portal_Egresso.domain.usecases;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.EgressCourseDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.EgressCourse;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.CourseRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.EgressCourseRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.EgressRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.usecases.exeptions.CourseNotFoundException;
import com.gabrielcsilva1.Portal_Egresso.domain.usecases.exeptions.EgressNotFoundException;
import com.gabrielcsilva1.Portal_Egresso.domain.usecases.exeptions.InvalidEndYearException;

@Service
public class RegisterEgressInCourseUseCase {
  @Autowired
  private EgressRepository egressRepository;

  @Autowired
  private CourseRepository courseRepository;

  @Autowired
  private EgressCourseRepository egressCourseRepository;

  public EgressCourse execute(EgressCourseDTO egressCourseDTO) {
    var egress = this.egressRepository.findById(egressCourseDTO.getEgressId());

    if (egress.isEmpty()) {
      throw new EgressNotFoundException();
    }

    var course = this.courseRepository.findById(egressCourseDTO.getCourseId());

    if (course.isEmpty()) {
      throw new CourseNotFoundException();
    }

    boolean isStartYearGreaterThanEndYear = false;
    boolean isEndYearGreaterThanCurrentYear = false;

    if (egressCourseDTO.getEndYear() != null) {
      isStartYearGreaterThanEndYear = egressCourseDTO.getStartYear().intValue() > egressCourseDTO.getEndYear().intValue();
      isEndYearGreaterThanCurrentYear = egressCourseDTO.getEndYear().intValue() > LocalDate.now().getYear();
    }

    if (isStartYearGreaterThanEndYear || isEndYearGreaterThanCurrentYear) {
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
}
