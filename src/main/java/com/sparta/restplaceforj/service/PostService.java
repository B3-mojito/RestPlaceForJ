package com.sparta.restplaceforj.service;

import com.sparta.restplaceforj.dto.PageResponseDto;
import com.sparta.restplaceforj.dto.PostIdTitleDto;
import com.sparta.restplaceforj.dto.PostRequestDto;
import com.sparta.restplaceforj.dto.PostResponseDto;
import com.sparta.restplaceforj.entity.Post;
import com.sparta.restplaceforj.entity.ThemeEnum;
import com.sparta.restplaceforj.entity.User;
import com.sparta.restplaceforj.exception.CommonException;
import com.sparta.restplaceforj.exception.ErrorEnum;
import com.sparta.restplaceforj.repository.PostDslRepository;
import com.sparta.restplaceforj.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 추천글 서비스.
 */
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PostService {

  private final PostRepository postRepository;
  private final PostDslRepository postDslRepository;

  /**
   * 글 생성
   *
   * @param postRequestDto : title, content, address, theme, placeName
   * @param user           : 토큰 유저 정보
   * @return PostResponseDto : title, content, address, likesCount, viewCount, themeEnum
   */
  @Transactional
  public PostResponseDto createPost(PostRequestDto postRequestDto, User user) {

    try {
      ThemeEnum.valueOf(postRequestDto.getTheme());
    } catch (IllegalArgumentException e) {
      throw new CommonException(ErrorEnum.THEME_NOT_FOUND);
    }

    Post post = postRepository.save(
        Post.builder()
            .requestDto(postRequestDto)
            .user(user)
            .build());

    Post savedPost = postRepository.save(post);

    return PostResponseDto.builder()
        .post(savedPost)
        .build();
  }

  /**
   * 삭제
   *
   * @param postId pathVariable
   * @param user   : 토큰 유저 정보
   */
  @Transactional
  public void deletePost(long postId, User user) {
    Post post = postRepository.findByIdOrThrow(postId);
    if (post.getUser().getId() != user.getId()) {
      throw new CommonException(ErrorEnum.POST_MISMATCH);
    }

    postRepository.deleteById(postId);
  }

  public PageResponseDto<String> getPlaceList(int page, int size, String region, String theme) {

    ThemeEnum themeEnum = ThemeEnum.valueOf(theme);
    Pageable pageRequest = PageRequest.of(page, size);

    PageImpl<String> placeNameList = postDslRepository
        .getPostListGroupByPlaceName(pageRequest, region, themeEnum);

    return PageResponseDto.<String>builder()
        .page(placeNameList)
        .build();
  }

  /**
   * 글을 조회하면 제목과 아이디만 반환  .
   *
   * @param page      현재 페이지
   * @param size      페이지 크기
   * @param placeName 장소명
   * @param q         검색질문
   * @param sortBy    정렬 기준
   * @return PageResponseDto : placeNameList, size, page, totalPages, totalElements
   */
  public PageResponseDto<PostIdTitleDto> getPostTitleList(
      int page, int size, String placeName, String sortBy, String q) {

    if (!(sortBy.equals("createAt") || sortBy.equals("viewsCount") ||
        sortBy.equals("likesCount"))) {
      throw new CommonException(ErrorEnum.SORT_NOT_FOUND);
    }

    Sort sort = Sort.by(Direction.DESC, sortBy);

    Pageable pageRequest = PageRequest.of(page, size, sort);

    PageImpl<PostIdTitleDto> postIdTitleList = postDslRepository
        .getPostTitleList(pageRequest, placeName, q);

    return PageResponseDto.<PostIdTitleDto>builder()
        .page(postIdTitleList)
        .build();
  }

  /**
   * 글 수정
   *
   * @param postId         수정 글 아이디
   * @param postRequestDto title, content, address, theme, placeName
   * @param user           : 토큰 유저 정보
   * @return PostResponseDto id, userId, title, content, address, likesCount, viewsCount, themeEnum
   */
  @Transactional
  public PostResponseDto updatePost(long postId, PostRequestDto postRequestDto, User user) {
    Post post = postRepository.findByIdOrThrow(postId);
    if (post.getUser().getId() != user.getId()) {
      throw new CommonException(ErrorEnum.POST_MISMATCH);
    }

    post.update(postRequestDto);
    return PostResponseDto.builder()
        .post(post)
        .build();
  }

  /**
   * 단권글 조회.
   *
   * @param postId 조회 글 아이디
   * @return PostResponseDto :id, userId, title, content, address, likesCount, viewsCount, themeEnum
   */
  @Transactional
  public PostResponseDto getPost(long postId) {
    Post post = postRepository.findByIdOrThrow(postId);
    post.addViewToPost();

    return PostResponseDto.builder()
        .post(post)
        .build();
  }
}
