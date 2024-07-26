package com.sparta.restplaceforj.service;

import com.sparta.restplaceforj.dto.PostRequestDto;
import com.sparta.restplaceforj.dto.PostResponseDto;
import com.sparta.restplaceforj.entity.Post;
import com.sparta.restplaceforj.exception.CommonException;
import com.sparta.restplaceforj.exception.ErrorEnum;
import com.sparta.restplaceforj.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PostService {

  private final PostRepository postRepository;

  @Transactional
  public PostResponseDto createPost(PostRequestDto postRequestDto) {

    Post post = postRepository.save(
        Post.builder()
            .requestDto(postRequestDto)
            .build());

    Post savedPost = postRepository.save(post);

    postRepository.findById(6L).orElseThrow(
        () -> new CommonException(ErrorEnum.POST_NOT_FOUND)
    );

    return new PostResponseDto(savedPost);
  }

  @Transactional
  public void deletePost(long postId) {
    postRepository.deleteById(postId);
  }
}
