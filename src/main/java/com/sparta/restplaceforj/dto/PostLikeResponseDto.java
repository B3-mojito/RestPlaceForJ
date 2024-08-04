package com.sparta.restplaceforj.dto;

import com.sparta.restplaceforj.entity.PostLike;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostLikeResponseDto {

  private final long id;
  private final long userId;
  private final long postId;

  @Builder
  public PostLikeResponseDto(PostLike postLike) {
    id = postLike.getId();
    userId = postLike.getUser().getId();
    postId = postLike.getPost().getId();
  }
}
