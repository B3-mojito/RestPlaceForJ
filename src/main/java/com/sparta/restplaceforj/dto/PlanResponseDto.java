package com.sparta.restplaceforj.dto;

import com.sparta.restplaceforj.entity.Plan;
import com.sparta.restplaceforj.entity.User;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


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
