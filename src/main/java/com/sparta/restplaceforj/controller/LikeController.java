package com.sparta.restplaceforj.controller;


import com.sparta.restplaceforj.common.CommonResponse;
import com.sparta.restplaceforj.common.ResponseEnum;
import com.sparta.restplaceforj.dto.CommentLikeResponseDto;
import com.sparta.restplaceforj.dto.PostLikeResponseDto;
import com.sparta.restplaceforj.security.UserDetailsImpl;
import com.sparta.restplaceforj.service.LikeService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikeController {

  private final LikeService likeService;

  /**
   * 글 좋아요(2번은 삭제).
   *
   * @param postId      좋아요 할 글
   * @param userDetails 좋아요 누른 사람
   * @return PostLikeResponseDto 필드명 : id, userId, postId
   */
  @PostMapping("/posts/{post-id}/likes")
  public ResponseEntity<CommonResponse> createPostLike(
      @PathVariable("post-id") long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    Optional<PostLikeResponseDto> optionalPostLikeResponseDto = likeService
        .createPostLike(postId, userDetails.getUser());

    ResponseEnum responseEnum =
        optionalPostLikeResponseDto.isPresent() ? ResponseEnum.CREATE_POST_LIKE_COMMENT
            : ResponseEnum.DELETE_POST_LIKE_COMMENT;

    return ResponseEntity.ok(CommonResponse.builder()
        .response(responseEnum)
        .data(optionalPostLikeResponseDto.orElse(null))
        .build());
  }

  /**
   * 댓글 좋아요 (2번 취소).
   *
   * @param commentId   좋아요 할 댓글
   * @param userDetails 좋아요 누른 사람
   * @return CommentLikeResponseDto 필드명 : id, userId, commentId
   */
  @PostMapping("/comments/{comment-id}/likes")
  public ResponseEntity<CommonResponse> createCommentLike(
      @PathVariable("comment-id") long commentId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    Optional<CommentLikeResponseDto> optionalCommentLikeResponseDto = likeService
        .createCommentLike(commentId, userDetails.getUser());

    ResponseEnum responseEnum =
        optionalCommentLikeResponseDto.isPresent() ? ResponseEnum.CREATE_COMMENT_LIKE_COMMENT
            : ResponseEnum.DELETE_COMMENT_LIKE_COMMENT;

    return ResponseEntity.ok(CommonResponse.builder()
        .response(responseEnum)
        .data(optionalCommentLikeResponseDto.orElse(null))
        .build());
  }
}
