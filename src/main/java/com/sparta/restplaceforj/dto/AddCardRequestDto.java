package com.sparta.restplaceforj.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.restplaceforj.entity.ThemeEnum;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddCardRequestDto {

  private Long planId;

  private Long cardId;

  private String address;

  private String placeName;

  private ThemeEnum themeEnum;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private LocalTime startedAt;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private LocalTime endedAt;
  
  private String memo;
}
