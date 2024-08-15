package com.sparta.restplaceforj.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserProfileResponseDto {

  private final Long userId;
  private final String nickname;
  private final String bio;
  private final String profileImage;

  @Builder
  public UserProfileResponseDto(Long userId, String nickname, String bio, String profileImage) {
    this.userId = userId;
    this.nickname = nickname;
    this.bio = bio;
    this.profileImage = profileImage;
  }
}
