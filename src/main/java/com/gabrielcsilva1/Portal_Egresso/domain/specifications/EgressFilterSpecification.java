package com.gabrielcsilva1.Portal_Egresso.domain.specifications;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Egress;

public class EgressFilterSpecification {
  public static Specification<Egress> didEgressTakenCourseInTheYear(Integer year) {
    return (root, _, builder) -> {
      if (ObjectUtils.isEmpty(year)) {
        return null;
      }

      var egressCourseJoin = root.join("egressCourse");

      var isStartYearGreaterOrEqualThanYear = builder.le(egressCourseJoin.get("startYear"), year);

      var isEndYearLessThanOrEqualThanYear = builder.or(
        builder.isNull(egressCourseJoin.get("endYear")),
        builder.ge(egressCourseJoin.get("endYear"), year)
      );

      return builder.and(
        isStartYearGreaterOrEqualThanYear,
        isEndYearLessThanOrEqualThanYear
      );
    };
  }

  public static Specification<Egress> isNameEqualTo(String name) {
    return (root, _, builder) -> {
      if (ObjectUtils.isEmpty(name)) {
        return null;
      }

      var nameFieldInLowerCase = builder.lower(root.get("name"));

      return builder.like(nameFieldInLowerCase, "%" + name.toLowerCase() + "%");
    };
  }

  public static Specification<Egress> didEgressTakenTheCourseByName(String courseName) {
    return (root, _, builder) -> {
      if (ObjectUtils.isEmpty(courseName)) {
        return null;
      }

      var courseJoin = root.join("egressCourse").join("course");

      var courseNameInLowerCase = builder.lower(courseJoin.get("name"));

      return builder.like(courseNameInLowerCase, "%" + courseName.toLowerCase() + "%");
    };
  }

  public static Specification<Egress> didEgressHoldPositionNamed(String positionName) {
    return (root, _, builder) -> {
      if (ObjectUtils.isEmpty(positionName)) {
        return null;
      }

      var positionJoin = root.join("positions");
      var positionDescriptionInLowerCase = builder.lower(positionJoin.get("description"));

      return builder.like(positionDescriptionInLowerCase, "%" + positionName.toLowerCase() + "%");
    };
  }
}
