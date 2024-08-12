package com.sparta.restplaceforj.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateUserProfileImageResponseDto {
    Long userId;
    String email;
    String profileImage;
}
