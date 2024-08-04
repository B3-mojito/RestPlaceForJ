package com.sparta.restplaceforj.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AuthCheckRequestDto {
    @NotBlank(message = "인증 번호를 입력해 주세요")
    private String authCode;

}
