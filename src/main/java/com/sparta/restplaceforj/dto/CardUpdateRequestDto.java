package com.sparta.restplaceforj.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CardUpdateRequestDto {

  private String title;

  private String address;

  private String placeName;

  private LocalTime startedAt;

  private LocalTime endedAt;

  private String memo;

}
