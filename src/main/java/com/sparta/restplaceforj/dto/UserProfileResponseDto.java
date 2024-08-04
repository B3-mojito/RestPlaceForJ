package com.sparta.restplaceforj.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserProfileResponseDto {
    private final String nickname;
    private final String bio;
    private final String profileImage;

    @Builder
    public UserProfileResponseDto(String nickname, String bio, String profileImage) {
        this.nickname = nickname;
        this.bio = bio;
        this.profileImage = profileImage;
    }
}
