package com.sparta.restplaceforj.controller;

import com.sparta.restplaceforj.common.CommonResponse;
import com.sparta.restplaceforj.common.ResponseEnum;
import com.sparta.restplaceforj.dto.AddCardRequestDto;
import com.sparta.restplaceforj.dto.PageResponseDto;
import com.sparta.restplaceforj.dto.PostIdTitleDto;
import com.sparta.restplaceforj.dto.PostRequestDto;
import com.sparta.restplaceforj.dto.PostResponseDto;
import com.sparta.restplaceforj.security.UserDetailsImpl;
import com.sparta.restplaceforj.service.PostService;
import jakarta.validation.Valid;
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

/**
 * 여행 추천 글 api.
 */
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class PostController {

  private final PostService postService;

  /**
   * 글 생성 api
   *
   * @param postRequestDto : title, content, address, theme, placeName
   * @param userDetails    : 토큰 유저 정보
   * @return PostResponseDto : title, content, address, likesCount, viewCount, themeEnum,
   * nickName,profilePicture
   */
  @PostMapping("/posts")
  public ResponseEntity<CommonResponse<PostResponseDto>> createPost(
      @RequestBody PostRequestDto postRequestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {

    PostResponseDto postResponseDto = postService.createPost(postRequestDto, userDetails.getUser());

    return ResponseEntity.ok(
        CommonResponse.<PostResponseDto>builder()
            .response(ResponseEnum.CREATE_POST)
            .data(postResponseDto)
            .build()
    );
  }

  /**
   * 삭제 api
   *
   * @param postId      pathVariable
   * @param userDetails : 토큰 유저 정보
   * @return null
   */
  @DeleteMapping("/posts/{post-id}")
  public ResponseEntity<CommonResponse> deletePost(
      @PathVariable("post-id") long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    postService.deletePost(postId, userDetails.getUser());

    return ResponseEntity.ok(
        CommonResponse.<PostResponseDto>builder()
            .response(ResponseEnum.DELETE_POST)
            .build()
    );
  }

  /**
   * 글의 placeName 의로 그룹화하여 갯수가 많은순으로 정렬 api. \
   *
   * @param page   현재 페이지
   * @param size   페이지 크기
   * @param region 받아온 주소
   * @param theme  여행 테마
   * @return PageResponseDto : placeNameList, size, page, totalPages, totalElements
   */
  @GetMapping("/posts/place-name")
  public ResponseEntity<CommonResponse<PageResponseDto<String>>> getPlaceList(
      @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
      @RequestParam String region, @RequestParam String theme) {

    PageResponseDto<String> postPageResponseDto = postService
        .getPlaceList(page, size, region, theme);

    return ResponseEntity.ok(
        CommonResponse.<PageResponseDto<String>>builder()
            .response(ResponseEnum.GET_POST_LIST)
            .data(postPageResponseDto)
            .build()
    );
  }

  /**
   * 글을 조회하면 제목과 아이디만 반환  api.
   *
   * @param page      현재 페이지
   * @param size      페이지 크기
   * @param placeName 장소명
   * @param q         검색질문
   * @param sortBy    정렬 기준
   * @return PageResponseDto : placeNameList, size, page, totalPages, totalElements
   */
  @GetMapping("/posts")
  public ResponseEntity<CommonResponse<PageResponseDto<PostIdTitleDto>>> getPostTitleList(
      @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
      @RequestParam("place-name") String placeName, @RequestParam(required = false) String q,
      @RequestParam(value = "sort-by", defaultValue = "createAt") String sortBy) {

    PageResponseDto<PostIdTitleDto> postPageResponseDto = postService
        .getPostTitleList(page, size, placeName, sortBy, q);

    return ResponseEntity.ok(
        CommonResponse.<PageResponseDto<PostIdTitleDto>>builder()
            .response(ResponseEnum.GET_POST_ID_TITLE_LIST)
            .data(postPageResponseDto)
            .build()
    );
  }

  /**
   * @param userDetails 조회할 대상
   * @param page        현재 페이지
   * @param size        페이지 크기
   * @param sortBy      정렬 기준
   * @return PageResponseDto : placeNameList, size, page, totalPages, totalElements
   */
  @GetMapping("/users/{user-id}/posts")
  public ResponseEntity<CommonResponse<PageResponseDto<PostIdTitleDto>>> getMyPostList(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
      @RequestParam(value = "sort-by", defaultValue = "createdAt") String sortBy) {
    PageResponseDto<PostIdTitleDto> postPageResponseDto = postService
        .getMyPostList(page, size, sortBy, userDetails.getUser().getId());

    return ResponseEntity.ok(
        CommonResponse.<PageResponseDto<PostIdTitleDto>>builder()
            .response(ResponseEnum.GET_MY_POST_LIST)
            .data(postPageResponseDto)
            .build()
    );
  }

  /**
   * 글 수정 api
   *
   * @param postId         수정 글 아이디
   * @param postRequestDto title, content, address, theme, placeName
   * @param userDetails    : 토큰 유저 정보
   * @return PostResponseDto : title, content, address, likesCount, viewCount, themeEnum,
   * nickName,profilePicture
   */
  @PatchMapping("/posts/{post-id}")
  public ResponseEntity<CommonResponse<PostResponseDto>> updatePost(
      @PathVariable("post-id") long postId, @RequestBody PostRequestDto postRequestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {

    PostResponseDto postResponseDto = postService
        .updatePost(postId, postRequestDto, userDetails.getUser());

    return ResponseEntity.ok(
        CommonResponse.<PostResponseDto>builder()
            .response(ResponseEnum.UPDATE_POST)
            .data(postResponseDto)
            .build()
    );
  }

  /**
   * 단권글 조회 api.
   *
   * @param postId 조회 글 아이디
   * @return PostResponseDto : title, content, address, likesCount, viewCount, themeEnum,
   * nickName,profilePicture
   */
  @GetMapping("/posts/{post-id}")
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

  /**
   * 게시물 카드에 추가  controller
   *
   * @param postId            게시물 아이디
   * @param addCardRequestDto 저장할 데이터 id, address, memo
   * @return PostResponseDto : id, userId, title, content, address, likesCount, viewsCount,
   * themeEnum
   */
  @PostMapping("/{post-id}")
  public ResponseEntity<CommonResponse<PostResponseDto>> cardAddPost(
      @PathVariable("post-id") Long postId,
      @RequestBody @Valid AddCardRequestDto addCardRequestDto) {

    PostResponseDto postResponseDto = postService.cardAddPost(postId, addCardRequestDto);

    return ResponseEntity.ok(
        CommonResponse.<PostResponseDto>builder()
            .response(ResponseEnum.ADD_POST)
            .data(postResponseDto)
            .build());
  }
}
