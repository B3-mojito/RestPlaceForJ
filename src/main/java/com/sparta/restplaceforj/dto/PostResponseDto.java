package com.sparta.restplaceforj.dto;

import com.sparta.restplaceforj.entity.Post;
import com.sparta.restplaceforj.entity.ThemeEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostResponseDto {

  private long id;

  private Long userId;

  private String nickName;

  private String profileImage;

  private String title;

  private String content;

  private String address;

  private long likesCount;

  private long viewsCount;

  private ThemeEnum themeEnum;

  private String placeName;

  @Builder
  public PostResponseDto(Post post) {
    id = post.getId();
    userId = post.getUser().getId();
    nickName = post.getUser().getNickname();
    profileImage = post.getUser().getProfileImage();
    title = post.getTitle();
    content = post.getContent();
    address = post.getAddress();
    likesCount = post.getLikesCount();
    viewsCount = post.getViewsCount();
    themeEnum = post.getThemeEnum();
    placeName = post.getPlaceName();
  }
}
