package com.sparta.restplaceforj.service;

import com.sparta.restplaceforj.dto.PostRequestDto;
import com.sparta.restplaceforj.dto.PostResponseDto;
import com.sparta.restplaceforj.entity.Post;
import com.sparta.restplaceforj.entity.ThemeEnum;
import com.sparta.restplaceforj.exception.CommonException;
import com.sparta.restplaceforj.exception.ErrorEnum;
import com.sparta.restplaceforj.repository.PostDslRepository;
import com.sparta.restplaceforj.repository.PostRepository;
import lombok.RequiredArgsConstructor;
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
}
