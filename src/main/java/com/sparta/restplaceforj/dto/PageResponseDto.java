package com.sparta.restplaceforj.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@NoArgsConstructor
public class PageResponseDto<T> {

  private List<T> contentList;
  private int size;
  private int page;
  private int totalPages;
  private long totalElements;

  @Builder
  public PageResponseDto(Page<T> page) {
    contentList = page.getContent();
    size = page.getSize();
    this.page = page.getNumber();
    totalElements = page.getTotalElements();
    totalPages = page.getTotalPages();
  }
}
