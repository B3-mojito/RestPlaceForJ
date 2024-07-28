package com.sparta.restplaceforj.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class PostPlaceNameResponseDto {

  private final String placeName;

}
