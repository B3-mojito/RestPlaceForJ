package com.sparta.restplaceforj.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import lombok.Getter;

@Getter
public class CardRequestDto {

  private String title;

  private String address;

  private String placeName;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private LocalTime startedAt;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private LocalTime endedAt;
  
  private String memo;

}
