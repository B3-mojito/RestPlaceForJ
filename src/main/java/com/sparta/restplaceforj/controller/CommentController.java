package com.sparta.restplaceforj.controller;

import com.sparta.restplaceforj.common.CommonResponse;
import com.sparta.restplaceforj.common.ResponseEnum;
import com.sparta.restplaceforj.dto.CommentRequestDto;
import com.sparta.restplaceforj.dto.CommentResponseDto;
import com.sparta.restplaceforj.dto.PageResponseDto;
import com.sparta.restplaceforj.security.UserDetailsImpl;
import com.sparta.restplaceforj.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/posts/{post-id}/comments")
public class CommentController {

  private final CommentService commentService;

  /**
   * 댓글 생성 api.
   *
   * @param commentRequestDto : content
   * @param postId            댓글 장성할 글
   * @return CommentResponseDto : id, postId, content, likesCount
   */
  @PostMapping
  public ResponseEntity<CommonResponse<CommentResponseDto>> createComment(
      @RequestBody CommentRequestDto commentRequestDto, @PathVariable("post-id") long postId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {

    CommentResponseDto commentResponseDto = commentService
        .createPost(postId, commentRequestDto.getContent(), userDetails.getUser());

    return ResponseEntity.ok(
        CommonResponse.<CommentResponseDto>builder()
            .response(ResponseEnum.CREATE_COMMENT)
            .data(commentResponseDto)
            .build()
    );
  }

  /**
   * 댓글 조회 api
   *
   * @param page   현재 페이지
   * @param size   페이지 사이즈
   * @param postId 조회할 글
   * @return PageResponseDto : contentList, size, page, totalPages, totalElements
   */
  @GetMapping
  public ResponseEntity<CommonResponse<PageResponseDto<CommentResponseDto>>> getCommentList(
      @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
      @PathVariable("post-id") long postId) {

    PageResponseDto<CommentResponseDto> commentResponseDtoList = commentService
        .getCommentList(page, size, postId);

    return ResponseEntity.ok(
        CommonResponse.<PageResponseDto<CommentResponseDto>>builder()
            .response(ResponseEnum.GET_COMMENT_LIST)
            .data(commentResponseDtoList)
            .build()
    );
  }

  /**
   * 댓글 수정 api.
   *
   * @param commentId         수정할 댓글
   * @param commentRequestDto : content
   * @param userDetails       작성자만 수정
   * @return : id, postId, content, likesCount
   */
  @PatchMapping("/{comment-id}")
  public ResponseEntity<CommonResponse<CommentResponseDto>> updateComment(
      @PathVariable("comment-id") long commentId, @RequestBody CommentRequestDto commentRequestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {

    CommentResponseDto commentResponseDto = commentService
        .updateComment(commentId, commentRequestDto.getContent(), userDetails.getUser().getId());

    return ResponseEntity.ok(
        CommonResponse.<CommentResponseDto>builder()
            .response(ResponseEnum.UPDATE_COMMENT)
            .data(commentResponseDto)
            .build()
    );
  }

  /**
   * 댓글 삭제 api.
   *
   * @param commentId   삭제할 댓글
   * @param userDetails 작성자
   * @return null
   */
  @DeleteMapping("/{comment-id}")
  public ResponseEntity<CommonResponse> deleteComment(@PathVariable("comment-id") long commentId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    commentService.deleteComment(commentId, userDetails.getUser().getId());

    return ResponseEntity.ok(
        CommonResponse.builder()
            .response(ResponseEnum.DELETE_COMMENT)
            .build()
    );
  }
}
