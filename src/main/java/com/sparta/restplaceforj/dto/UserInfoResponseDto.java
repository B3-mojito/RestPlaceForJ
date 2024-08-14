package com.sparta.restplaceforj.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserInfoResponseDto {

  private final Long userId;

  @Builder
  public UserInfoResponseDto(Long userId) {
    this.userId = userId;
  }
}
