package com.sparta.restplaceforj.dto;

import com.sparta.restplaceforj.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserSignUpResponseDto {

    private final String email;
    private final String name;
    private final String nickname;

    @Builder
    public UserSignUpResponseDto(User user) {
        this.email = user.getEmail();
        this.name = user.getName();
        this.nickname = user.getNickname();
    }
}
