package com.gabrielcsilva1.Portal_Egresso.dtos.paginated;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponsePaginated<T> {
  private List<T> content;
  private int pageIndex;
  private int totalPages;
  private long totalElements;
}
