package com.sparta.restplaceforj.dto;


import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;


@Getter
public class ColumnResponseDto {

    private final Long id;
    private final String title;
    private final LocalDate date;

    @Builder
    public ColumnResponseDto(Long id, String title, LocalDate date) {
        this.id = id;
        this.title = title;
        this.date = date;
    }
}
