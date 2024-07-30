package com.sparta.restplaceforj.dto;

import com.sparta.restplaceforj.entity.Comment;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentResponseDto {

  private final long id;

  private final long postId;

//  private final long userId;

  private final String content;

  private final long likesCount;

  @Builder
  public CommentResponseDto(Comment comment) {
    id = comment.getId();
    postId = comment.getPost().getId();
//    userId = comment.getUser().getId();
    content = comment.getContent();
    likesCount = comment.getLikesCount();
  }
}
