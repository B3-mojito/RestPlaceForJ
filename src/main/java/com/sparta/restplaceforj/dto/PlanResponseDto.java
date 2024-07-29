package com.sparta.restplaceforj.dto;

import com.sparta.restplaceforj.entity.Plan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
public class PlanResponseDto {

  private final Long id;
  private final String title;

  @Builder
  public PlanResponseDto(Long id, String title) {
    this.id = id;
    this.title = title;
  }
}
