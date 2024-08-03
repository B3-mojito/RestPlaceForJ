package com.sparta.restplaceforj.dto;

import com.sparta.restplaceforj.entity.CommentLike;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentLikeResponseDto {

  private final long id;
  private final long userId;
  private final long commentId;

  @Builder
  public CommentLikeResponseDto(CommentLike commentLike) {
    id = commentLike.getId();
    userId = commentLike.getUser().getId();
    commentId = commentLike.getComment().getId();
  }
}
