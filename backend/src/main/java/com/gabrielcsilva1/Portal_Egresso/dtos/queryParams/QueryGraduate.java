package com.gabrielcsilva1.Portal_Egresso.dtos.queryParams;

import static com.gabrielcsilva1.Portal_Egresso.dtos.queryParams.builders.GraduateQueryBuilder.*;

import org.springframework.data.jpa.domain.Specification;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Graduate;
import com.gabrielcsilva1.Portal_Egresso.dtos.enums.StatusEnum;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QueryGraduate {
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
      .and(didGraduateTakenTheCourseByName(courseName))
      .and(isRegistrationStatus(StatusEnum.ACCEPTED));
  }
}
