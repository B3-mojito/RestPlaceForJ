package com.sparta.restplaceforj.controller;

import com.sparta.restplaceforj.common.CommonResponse;
import com.sparta.restplaceforj.common.ResponseEnum;
import com.sparta.restplaceforj.dto.PostPlaceNameResponseDto;
import com.sparta.restplaceforj.dto.PostRequestDto;
import com.sparta.restplaceforj.dto.PostResponseDto;
import com.sparta.restplaceforj.service.PostService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 여행 추천 글 api.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/posts")
public class PostController {

  private final PostService postService;

  /**
   * 글 생성 api.
   */
  @PostMapping
  public ResponseEntity<CommonResponse<PostResponseDto>> createPost(
      @RequestBody PostRequestDto postRequestDto) {

    PostResponseDto postResponseDto = postService.createPost(postRequestDto);

    return ResponseEntity.ok(
        CommonResponse.<PostResponseDto>builder()
            .response(ResponseEnum.CREATE_POST)
            .data(postResponseDto)
            .build()
    );
  }

  /**
   * 글 삭제 api.
   */
  @DeleteMapping("/{post-id}")
  public ResponseEntity<CommonResponse> deletePost(@PathVariable("post-id") long postId) {
    postService.deletePost(postId);

    return ResponseEntity.ok(
        CommonResponse.<PostResponseDto>builder()
            .response(ResponseEnum.DELETE_POST)
            .build()
    );
  }

  @GetMapping("/place-name")
  public ResponseEntity<CommonResponse<List<PostPlaceNameResponseDto>>> getPostList(
      @RequestParam int page, @RequestParam(defaultValue = "5") int size,
      @RequestParam("sort-address") String sortAddress, @RequestParam String theme) {

    List<PostPlaceNameResponseDto> postResponseDtoList = postService
        .getPostList(page, size, sortAddress, theme);

    return ResponseEntity.ok(
        CommonResponse.<List<PostPlaceNameResponseDto>>builder()
            .response(ResponseEnum.GET_POST_LIST)
            .data(postResponseDtoList)
            .build()
    );
  }
}
