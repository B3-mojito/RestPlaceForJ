package com.sparta.restplaceforj.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateUserProfileImageResponseDto {
    String profileImageUrl;
}
