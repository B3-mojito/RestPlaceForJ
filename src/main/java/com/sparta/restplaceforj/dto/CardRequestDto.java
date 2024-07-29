package com.sparta.restplaceforj.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import lombok.Getter;

@Getter
public class CardRequestDto {

  @NotNull
  private Long columnId;

  @NotBlank(message = "카드 제목을 입력해 주세요")
  private String title;

  @NotBlank(message = "주소를 입력해 주세요.")
  private String address;

  @NotBlank(message = "장소를 입력해 주세요.")
  private String placeName;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private LocalTime startedAt;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private LocalTime endedAt;

  @NotBlank(message = "메모를 입력해 주세요.")
  private String memo;

}
