package com.sparta.restplaceforj.dto;

import lombok.Builder;
import lombok.Getter;


@Getter
public class UserImageResponseDto {

  private final Long id;
  private final String imageUrl;

  @Builder
  public UserImageResponseDto(Long id, String imageUrl) {
    this.id = id;
    this.imageUrl = imageUrl;
  }
}
