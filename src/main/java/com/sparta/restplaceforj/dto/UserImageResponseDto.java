package com.sparta.restplaceforj.dto;

import lombok.Builder;
import lombok.Getter;


@Getter
public class UserImageResponseDto {

  private final Long id;
  private final String imageUrl;
  private final String nickname;

  @Builder
  public UserImageResponseDto(Long id, String imageUrl, String nickname) {
    this.id = id;
    this.imageUrl = imageUrl;
    this.nickname = nickname;
  }
}
