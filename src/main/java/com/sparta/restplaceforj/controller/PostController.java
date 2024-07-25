package com.sparta.restplaceforj.controller;

import com.sparta.restplaceforj.common.CommonResponse;
import com.sparta.restplaceforj.common.ResponseEnum;
import com.sparta.restplaceforj.dto.PostRequestDto;
import com.sparta.restplaceforj.dto.PostResponseDto;
import com.sparta.restplaceforj.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

  private final PostService postService;


  @PostMapping
  public ResponseEntity<CommonResponse<PostResponseDto>> createPost(

      @RequestBody PostRequestDto requestDto) {

    PostResponseDto responseDto = postService.createPost(
        requestDto);

    return ResponseEntity.ok(
        CommonResponse.<PostResponseDto>builder()
            .response(ResponseEnum.CREATE_POST)
            .data(responseDto)
            .build()
    );
  }
}
