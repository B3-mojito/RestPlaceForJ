package com.sparta.restplaceforj.service;

import com.sparta.restplaceforj.entity.Image;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ImageResponseDto {

  private final long id;
  private final String path;
  private final String originalFileName;
  private final String changedFileName;

  @Builder
  public ImageResponseDto(Image image) {
    this.id = image.getId();
    this.path = image.getPath();
    this.originalFileName = image.getOriginalFileName();
    this.changedFileName = image.getChangedFileName();
  }
}
