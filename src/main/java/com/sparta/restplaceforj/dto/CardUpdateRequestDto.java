package com.sparta.restplaceforj.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
public class CardUpdateRequestDto {

  private String title;

  private String address;

  private String placeName;

  private LocalDateTime startedAt;

  private LocalDateTime endedAt;

  private String memo;

}
