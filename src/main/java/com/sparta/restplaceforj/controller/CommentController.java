package com.sparta.restplaceforj.controller;

import com.sparta.restplaceforj.common.CommonResponse;
import com.sparta.restplaceforj.common.ResponseEnum;
import com.sparta.restplaceforj.dto.CommentRequestDto;
import com.sparta.restplaceforj.dto.CommentResponseDto;
import com.sparta.restplaceforj.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/posts/{post-id}/comments")
public class CommentController {

  private final CommentService commentService;

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
}
