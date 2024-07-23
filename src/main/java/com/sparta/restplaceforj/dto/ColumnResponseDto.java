package com.sparta.restplaceforj.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ColumnResponseDto {
    private String title;

    @Builder
    public ColumnResponseDto(String title) {
        this.title = title;
    }
}
