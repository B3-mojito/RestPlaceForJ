package com.sparta.restplaceforj.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddCardRequestDto {

  private Long planId;

  private Long cardId;

  @NotBlank(message = "장소를 입력해 주세요.")
  private String placeName;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private LocalTime startedAt;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private LocalTime endedAt;

  @NotBlank(message = "메모를 입력해 주세요.")
  private String memo;
}
