package com.sparta.restplaceforj.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AuthCheckResponseDto {
    private final Long coworkerId;

    @Builder
    public AuthCheckResponseDto(Long coworkerId) {
        this.coworkerId = coworkerId;
    }
}
