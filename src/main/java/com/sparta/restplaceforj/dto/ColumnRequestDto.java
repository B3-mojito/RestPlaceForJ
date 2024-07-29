package com.sparta.restplaceforj.dto;

import java.time.LocalDate;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
public class ColumnRequestDto {

  private String title;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate date;

}
