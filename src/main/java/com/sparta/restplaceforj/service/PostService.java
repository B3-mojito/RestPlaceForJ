package com.sparta.restplaceforj.service;

import com.sparta.restplaceforj.dto.PostPlaceNameResponseDto;
import com.sparta.restplaceforj.dto.PostRequestDto;
import com.sparta.restplaceforj.dto.PostResponseDto;
import com.sparta.restplaceforj.entity.Post;
import com.sparta.restplaceforj.entity.ThemeEnum;
import com.sparta.restplaceforj.exception.CommonException;
import com.sparta.restplaceforj.exception.ErrorEnum;
import com.sparta.restplaceforj.repository.PostDslRepository;
import com.sparta.restplaceforj.repository.PostRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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

    return new PostResponseDto(savedPost);
  }

  @Transactional
  public void deletePost(long postId) {
    postRepository.deleteById(postId);
  }

  public List<PostPlaceNameResponseDto> getPostList(
      int page, int size, String sortAddress, String theme) {

    ThemeEnum themeEnum = ThemeEnum.valueOf(theme);
    PageRequest pageRequest = PageRequest.of(page, size);
    List<String> postList = postDslRepository
        .getPostListGroupByPlaceName(pageRequest, sortAddress, themeEnum);

    return postList.stream()
        .map(PostPlaceNameResponseDto::new)
        .toList();
  }
}
