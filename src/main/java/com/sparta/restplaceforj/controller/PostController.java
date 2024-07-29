package com.sparta.restplaceforj.controller;

import com.sparta.restplaceforj.common.CommonResponse;
import com.sparta.restplaceforj.common.ResponseEnum;
import com.sparta.restplaceforj.dto.PostPageResponseDto;
import com.sparta.restplaceforj.dto.PostRequestDto;
import com.sparta.restplaceforj.dto.PostResponseDto;
import com.sparta.restplaceforj.service.PostService;
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

  /**
   * 글의 placeName 의로 그룹화하여 갯수가 많은순으로 정렬 api.
   */
  @GetMapping("/place-name")
  public ResponseEntity<CommonResponse<PostPageResponseDto>> getPostList(
      @RequestParam int page, @RequestParam(defaultValue = "5") int size,
      @RequestParam("short-address") String shortAddress, @RequestParam String theme) {

    PostPageResponseDto postPageResponseDto = postService
        .getPostList(page, size, shortAddress, theme);

    return ResponseEntity.ok(
        CommonResponse.<PostPageResponseDto>builder()
            .response(ResponseEnum.GET_POST_LIST)
            .data(postPageResponseDto)
            .build()
    );
  }

  /**
   * 글을 조회하면 제목과 아이디만 반환.
   */
  @GetMapping
  public ResponseEntity<CommonResponse<PostPageResponseDto>> getPostTitleList(
      @RequestParam int page, @RequestParam(defaultValue = "5") int size,
      @RequestParam("place-name") String placeName, @RequestParam(required = false) String q,
      @RequestParam(value = "sort-by", defaultValue = "createAt") String sortBy) {

    PostPageResponseDto postPageResponseDto = postService
        .getPostTitleList(page, size, placeName, sortBy, q);

    return ResponseEntity.ok(
        CommonResponse.<PostPageResponseDto>builder()
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
}
