package com.sparta.restplaceforj.dto;

import com.sparta.restplaceforj.entity.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResignResponseDto {
    private final String email;
    private final UserRole role;

    @Builder
    public UserResignResponseDto(String email, UserRole role) {
        this.email = email;
        this.role = role;
    }
}
