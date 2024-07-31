package com.sparta.restplaceforj.controller;

import com.sparta.restplaceforj.common.CommonResponse;
import com.sparta.restplaceforj.common.ResponseEnum;
import com.sparta.restplaceforj.dto.PageResponseDto;
import com.sparta.restplaceforj.dto.PostRequestDto;
import com.sparta.restplaceforj.dto.PostResponseDto;
import com.sparta.restplaceforj.service.ImageResponseDto;
import com.sparta.restplaceforj.service.PostService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

  /**
   * 글의 placeName 의로 그룹화하여 갯수가 많은순으로 정렬 api.
   */
  @GetMapping("/place-name")
  public ResponseEntity<CommonResponse<PageResponseDto>> getPlaceList(
      @RequestParam int page, @RequestParam(defaultValue = "5") int size,
      @RequestParam String region, @RequestParam String theme) {

    PageResponseDto postPageResponseDto = postService
        .getPlaceList(page, size, region, theme);

    return ResponseEntity.ok(
        CommonResponse.<PageResponseDto>builder()
            .response(ResponseEnum.GET_POST_LIST)
            .data(postPageResponseDto)
            .build()
    );
  }

  /**
   * 글을 조회하면 제목과 아이디만 반환.
   */
  @GetMapping
  public ResponseEntity<CommonResponse<PageResponseDto>> getPostTitleList(
      @RequestParam int page, @RequestParam(defaultValue = "5") int size,
      @RequestParam("place-name") String placeName, @RequestParam(required = false) String q,
      @RequestParam(value = "sort-by", defaultValue = "createAt") String sortBy) {

    PageResponseDto postPageResponseDto = postService
        .getPostTitleList(page, size, placeName, sortBy, q);

    return ResponseEntity.ok(
        CommonResponse.<PageResponseDto>builder()
            .response(ResponseEnum.GET_POST_ID_TITLE_LIST)
            .data(postPageResponseDto)
            .build()
    );
  }

  /**
   * 글 수정.
   */
  @PatchMapping("/{post-id}")
  public ResponseEntity<CommonResponse<PostResponseDto>> updatePost(
      @PathVariable("post-id") long postId, @RequestBody PostRequestDto postRequestDto) {

    PostResponseDto postResponseDto = postService.updatePost(postId, postRequestDto);

    return ResponseEntity.ok(
        CommonResponse.<PostResponseDto>builder()
            .response(ResponseEnum.UPDATE_POST)
            .data(postResponseDto)
            .build()
    );
  }

  /**
   * 글 단권 조회.
   */
  @GetMapping("/{post-id}")
  public ResponseEntity<CommonResponse<PostResponseDto>> getPost(
      @PathVariable("post-id") long postId) {
    PostResponseDto postResponseDto = postService.getPost(postId);

    return ResponseEntity.ok(
        CommonResponse.<PostResponseDto>builder()
            .response(ResponseEnum.GET_POST)
            .data(postResponseDto)
            .build()
    );
  }

  @PostMapping(value = "/images")
  public ResponseEntity<CommonResponse<ImageResponseDto>> createPostImage(
      @RequestPart("images") MultipartFile images) throws IOException {

    ImageResponseDto imageResponseDto = postService.createPostImage(images);

    return ResponseEntity.ok(
        CommonResponse.<ImageResponseDto>builder()
            .response(ResponseEnum.CREATE_USER_PROFILE_IMAGE)
            .data(imageResponseDto)
            .build()
    );
  }
}
