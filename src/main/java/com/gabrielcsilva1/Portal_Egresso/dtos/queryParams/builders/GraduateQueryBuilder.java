package com.gabrielcsilva1.Portal_Egresso.dtos.queryParams.builders;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Graduate;
import com.gabrielcsilva1.Portal_Egresso.dtos.enums.StatusEnum;

import jakarta.persistence.criteria.JoinType;

public class GraduateQueryBuilder {
  public static Specification<Graduate> didGraduateTakenCourseInTheYear(Integer year) {
    return (root, _, builder) -> {
      if (ObjectUtils.isEmpty(year)) {
        return null;
      }

      var graduateCoursesJoin = root.join("graduateCourses");

      var isStartYearGreaterOrEqualThanYear = builder.le(graduateCoursesJoin.get("startYear"), year);

      var isEndYearLessThanOrEqualThanYear = builder.or(
        builder.isNull(graduateCoursesJoin.get("endYear")),
        builder.ge(graduateCoursesJoin.get("endYear"), year)
      );

      return builder.and(
        isStartYearGreaterOrEqualThanYear,
        isEndYearLessThanOrEqualThanYear
      );
    };
  }

  public static Specification<Graduate> isNameEqualTo(String name) {
    return (root, _, builder) -> {
      if (ObjectUtils.isEmpty(name)) {
        return null;
      }

      var nameFieldInLowerCase = builder.lower(root.get("name"));

      return builder.like(nameFieldInLowerCase, "%" + name.toLowerCase() + "%");
    };
  }

  public static Specification<Graduate> didGraduateTakenTheCourseByName(String courseName) {
    return (root, _, builder) -> {
      if (ObjectUtils.isEmpty(courseName)) {
        return null;
      }

      var courseJoin = root.join("graduateCourses").join("course");

      var courseNameInLowerCase = builder.lower(courseJoin.get("name"));

      return builder.like(courseNameInLowerCase, "%" + courseName.toLowerCase() + "%");
    };
  }

  public static Specification<Graduate> didGraduateHoldPositionNamed(String positionName) {
    return (root, _, builder) -> {
      if (ObjectUtils.isEmpty(positionName)) {
        return null;
      }

      var positionJoin = root.join("positions", JoinType.LEFT); 
      var positionDescriptionInLowerCase = builder.lower(positionJoin.get("description"));

      return builder.like(positionDescriptionInLowerCase, "%" + positionName.toLowerCase() + "%");
    };
  }

  public static Specification<Graduate> isRegistrationStatus(StatusEnum registrationStatus) {
    return (root, _, builder) -> {
      if (ObjectUtils.isEmpty(registrationStatus)) {
        return null;
      }

      return builder.equal(root.get("registrationStatus"), registrationStatus);
    };
  }
}
