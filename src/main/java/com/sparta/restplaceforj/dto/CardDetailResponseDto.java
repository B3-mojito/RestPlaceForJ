package com.sparta.restplaceforj.dto;

import java.time.LocalTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CardDetailResponseDto {

  private final Long id;
  private final String title;
  private final String address;
  private final String placeName;
  private final LocalTime startedAt;
  private final LocalTime endedAt;
  private final String memo;
  private final List<PostResponseDto> postList;
}
