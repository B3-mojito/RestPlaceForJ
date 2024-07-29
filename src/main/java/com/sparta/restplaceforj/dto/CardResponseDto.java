package com.sparta.restplaceforj.dto;

import com.sparta.restplaceforj.entity.CardTimestamped;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Builder
public class CardResponseDto {

  private final Long id;
  private final String title;
  private final String address;
  private final String placeName;
  private final LocalDateTime startedAt;
  private final LocalDateTime endedAt;
  private final String memo;
}
