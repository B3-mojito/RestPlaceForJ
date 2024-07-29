package com.sparta.restplaceforj.dto;

import com.sparta.restplaceforj.entity.Plan;
import lombok.Builder;
import lombok.Getter;


@Getter
public class PlanResponseDto {

    private final String title;

    @Builder
    public PlanResponseDto(Plan plan) {
        this.title = plan.getTitle();
    }
}
