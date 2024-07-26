package com.sparta.restplaceforj.dto;

import java.time.LocalDate;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class ColumnResponseDto {

  private String title;
  private LocalDate date;
}
