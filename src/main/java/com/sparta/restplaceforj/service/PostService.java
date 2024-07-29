package com.sparta.restplaceforj.service;

import com.sparta.restplaceforj.dto.PostIdTitleDto;
import com.sparta.restplaceforj.dto.PostPageResponseDto;
import com.sparta.restplaceforj.dto.PostRequestDto;
import com.sparta.restplaceforj.dto.PostResponseDto;
import com.sparta.restplaceforj.entity.Post;
import com.sparta.restplaceforj.entity.ThemeEnum;
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
   * 글 새성.
   */
  @Transactional
  public PostResponseDto createPost(PostRequestDto postRequestDto) {

    try {
      ThemeEnum.valueOf(postRequestDto.getTheme());
    } catch (IllegalArgumentException e) {
      throw new CommonException(ErrorEnum.THEME_NOT_FOUND);
    }

    Post post = postRepository.save(
        Post.builder()
            .requestDto(postRequestDto)
            .build());

    Post savedPost = postRepository.save(post);

    return PostResponseDto.builder()
        .post(savedPost)
        .build();
  }

  @Transactional
  public void deletePost(long postId) {
    postRepository.deleteById(postId);
  }

  /**
   * 글의 placeName 의로 그룹화하여 갯수가 많은순으로 정렬 api.
   */
  public PostPageResponseDto getPostList(
      int page, int size, String shortAddress, String theme) {

    ThemeEnum themeEnum = ThemeEnum.valueOf(theme);
    Pageable pageRequest = PageRequest.of(page, size);

    PageImpl<String> placeNameList = postDslRepository
        .getPostListGroupByPlaceName(pageRequest, shortAddress, themeEnum);

    return PostPageResponseDto.<String>builder()
        .page(placeNameList)
        .build();
  }

  /**
   * 글 아이디와 제목만 조회.
   */
  public PostPageResponseDto getPostTitleList(
      int page, int size, String placeName, String sortBy, String q) {

    if (!(sortBy.equals("createAt") || sortBy.equals("viewsCount") ||
        sortBy.equals("likesCount"))) {
      throw new CommonException(ErrorEnum.SORT_NOT_FOUND);
    }

    Sort sort = Sort.by(Direction.DESC, sortBy);

    Pageable pageRequest = PageRequest.of(page, size, sort);

    PageImpl<PostIdTitleDto> postIdTitleList = postDslRepository
        .getPostTitleList(pageRequest, placeName, q);

    return PostPageResponseDto.<PostIdTitleDto>builder()
        .page(postIdTitleList)
        .build();
  }

  /**
   * 글 수정.
   */
  @Transactional
  public PostResponseDto updatePost(long postId, PostRequestDto postRequestDto) {
    Post post = postRepository.findByIdOrThrow(postId);
    post.update(postRequestDto);
    return PostResponseDto.builder()
        .post(post)
        .build();
  }
}
