package com.gabrielcsilva1.Portal_Egresso.infra.queryfilters;

import static com.gabrielcsilva1.Portal_Egresso.domain.repositories.specifications.GraduateFilterSpecification.*;

import org.springframework.data.jpa.domain.Specification;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Graduate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GraduateQueryFilter {
  /*
   * query - name or position name
   */
  private String query;
  private String courseName;
  private Integer year;

  public Specification<Graduate> toSpecification() {
    var isNameOrPositionEqualToQuery = isNameEqualTo(query).or(didGraduateHoldPositionNamed(query));

    return isNameOrPositionEqualToQuery
      .and(didGraduateTakenCourseInTheYear(year))
      .and(didGraduateTakenTheCourseByName(courseName));
  }
}
