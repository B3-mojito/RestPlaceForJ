package com.sparta.restplaceforj.dto;

import java.time.LocalTime;
import lombok.Getter;


@Getter
public class CardUpdateRequestDto {

  private String title;

  private String address;

  private String placeName;

  private LocalTime startedAt;

  private LocalTime endedAt;

  private String memo;

}
