package com.sparta.restplaceforj.dto;

import com.sparta.restplaceforj.entity.Post;
import com.sparta.restplaceforj.entity.ThemeEnum;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostResponseDto {

  private final long id;
  // private final long userId;
  private final String title;
  private final String content;
  private final String address;
  private final long likesCount;
  private final long viewsCount;
  private final ThemeEnum themeEnum;

  @Builder
  public PostResponseDto(Post post) {
    this.id = post.getId();
    // this.userId = post.getUser().getId();
    this.title = post.getTitle();
    this.content = post.getContent();
    this.address = post.getAddress();
    this.likesCount = post.getLikesCount();
    this.viewsCount = post.getViewsCount();
    this.themeEnum = post.getThemeEnum();
  }
}
