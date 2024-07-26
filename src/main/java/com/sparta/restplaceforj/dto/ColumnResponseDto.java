package com.sparta.restplaceforj.dto;

import com.sparta.restplaceforj.entity.Column;
import java.time.LocalDate;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
public class ColumnResponseDto {

  private final String title;
  private final LocalDate date;

  @Builder
  public ColumnResponseDto(Column column) {
    this.title = column.getTitle();
    this.date = column.getDate();
  }
}
