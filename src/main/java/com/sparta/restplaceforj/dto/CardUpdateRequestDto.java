package com.sparta.restplaceforj.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CardUpdateRequestDto {

  private String title;

  private String address;

  private String placeName;

  private String memo;

}
