package com.sparta.restplaceforj.dto;

import com.sparta.restplaceforj.entity.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentResponseDto {

  private long id;

  private long postId;

  private long userId;

  private String content;

  private long likesCount;

  private String nickName;

  private String profileImage;

  @Builder
  public CommentResponseDto(Comment comment) {
    id = comment.getId();
    postId = comment.getPost().getId();
    userId = comment.getUser().getId();
    nickName = comment.getUser().getNickname();
    profileImage = comment.getUser().getProfileImage();
    content = comment.getContent();
    likesCount = comment.getLikesCount();
  }
}
