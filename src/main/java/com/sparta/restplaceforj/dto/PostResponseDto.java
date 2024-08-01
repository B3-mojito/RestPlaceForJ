package com.sparta.restplaceforj.dto;

import com.sparta.restplaceforj.entity.Post;
import com.sparta.restplaceforj.entity.ThemeEnum;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostResponseDto {

  private final long id;

  private final long userId;

  private final String nickName;

  private final String profilePicture;

  private final String title;

  private final String content;

  private final String address;

  private final long likesCount;

  private final long viewsCount;

  private final ThemeEnum themeEnum;

  @Builder
  public PostResponseDto(Post post) {
    id = post.getId();
    userId = post.getUser().getId();
    nickName = post.getUser().getNickname();
    profilePicture = post.getUser().getProfilePicture();
    title = post.getTitle();
    content = post.getContent();
    address = post.getAddress();
    likesCount = post.getLikesCount();
    viewsCount = post.getViewsCount();
    themeEnum = post.getThemeEnum();
  }
}
