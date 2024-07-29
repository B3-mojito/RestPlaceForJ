package com.sparta.restplaceforj.dto;

import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
public class ColumnRequestDto {

    private String title;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

}
