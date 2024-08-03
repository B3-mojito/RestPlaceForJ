package com.sparta.restplaceforj.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class PostRequestDto {

  private String title;

  private String content;

  private String address;

  private String theme;

  private String placeName;

  private List<Long> imageIdList;
}
