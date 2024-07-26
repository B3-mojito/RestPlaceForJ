package com.sparta.restplaceforj.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Builder
public class CardResponseDto {

  private Long id;
  private String title;
  private String address;
  private String placeName;
  private String memo;
}
