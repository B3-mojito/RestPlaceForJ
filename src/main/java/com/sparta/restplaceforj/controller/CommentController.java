package com.sparta.restplaceforj.controller;

import com.sparta.restplaceforj.common.CommonResponse;
import com.sparta.restplaceforj.common.ResponseEnum;
import com.sparta.restplaceforj.dto.CommentRequestDto;
import com.sparta.restplaceforj.dto.CommentResponseDto;
import com.sparta.restplaceforj.dto.PageResponseDto;
import com.sparta.restplaceforj.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/posts/{post-id}/comments")
public class CommentController {

  private final CommentService commentService;

  /**
   * 댓글 생성.
   */
  @PostMapping
  public ResponseEntity<CommonResponse<CommentResponseDto>> createComment(
      @RequestBody CommentRequestDto commentRequestDto, @PathVariable("post-id") long postId) {

    CommentResponseDto commentResponseDto = commentService
        .createPost(postId, commentRequestDto.getContent());

    return ResponseEntity.ok(
        CommonResponse.<CommentResponseDto>builder()
            .response(ResponseEnum.CREATE_COMMENT)
            .data(commentResponseDto)
            .build()
    );
  }

  /**
   * 댓글 조회.
   */
  @GetMapping
  public ResponseEntity<CommonResponse<PageResponseDto<CommentResponseDto>>> getCommentList(
      @RequestParam int page, @RequestParam(defaultValue = "5") int size,
      @PathVariable("post-id") long postId) {

    PageResponseDto<CommentResponseDto> commentResponseDtoList = commentService
        .getCommentList(page, size, postId);

    return ResponseEntity.ok(
        CommonResponse.<PageResponseDto<CommentResponseDto>>builder()
            .response(ResponseEnum.GET_COMMENT_LIST)
            .data(commentResponseDtoList)
            .build());
  }

  /**
   * 댓글 수정.
   */
  @PatchMapping("/{comment-id}")
  public ResponseEntity<CommonResponse<CommentResponseDto>> updateComment(
      @PathVariable("comment-id") long commentId,
      @RequestBody CommentRequestDto commentRequestDto) {

    CommentResponseDto commentResponseDto = commentService
        .updateComment(commentId, commentRequestDto.getContent());

    return ResponseEntity.ok(
        CommonResponse.<CommentResponseDto>builder()
            .response(ResponseEnum.UPDATE_COMMENT)
            .data(commentResponseDto)
            .build()
    );
  }
}
