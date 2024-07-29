package com.sparta.restplaceforj.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.sparta.restplaceforj.entity.CardTimestamped;
import com.sparta.restplaceforj.entity.Timestamped;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

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

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDateTime startedAt;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDateTime endedAt;

  @NotBlank(message = "메모를 입력해 주세요.")
  private String memo;

}
