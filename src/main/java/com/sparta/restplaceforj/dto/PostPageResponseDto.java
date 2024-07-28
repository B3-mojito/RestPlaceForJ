package com.sparta.restplaceforj.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class PostPageResponseDto<T> {

  private final List placeNameList;
  private final int size;
  private final int page;
  private final int totalPages;
  private final long totalElements;

  @Builder
  public PostPageResponseDto(Page<T> page) {
    placeNameList = page.getContent();
    size = page.getSize();
    this.page = page.getNumber();
    totalElements = page.getTotalElements();
    totalPages = page.getTotalPages();
  }
}
