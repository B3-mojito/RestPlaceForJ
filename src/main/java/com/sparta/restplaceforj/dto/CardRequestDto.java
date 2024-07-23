package com.sparta.restplaceforj.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CardRequestDto {
    @NotNull(message = "카드 제목을 입력해 주세요")
    private String title;

    @NotNull(message = "주소를 입력해 주세요.")
    private String address;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;
}
