package com.sparta.restplaceforj.service;

import com.sparta.restplaceforj.dto.PostLikeResponseDto;
import com.sparta.restplaceforj.entity.Post;
import com.sparta.restplaceforj.entity.PostLike;
import com.sparta.restplaceforj.entity.User;
import com.sparta.restplaceforj.repository.CommentLikeRepository;
import com.sparta.restplaceforj.repository.PostLikeRepository;
import com.sparta.restplaceforj.repository.PostRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {

  private final PostLikeRepository postLikeRepository;
  private final PostRepository postRepository;
  private final CommentLikeRepository commentLikeRepository;

  /**
   * 좋아요 증가&감소.
   */
  @Transactional
  public Optional<PostLikeResponseDto> createPostLike(long postId, User user) {
    Post post = postRepository.findByIdOrThrow(postId);
    PostLike postLike = PostLike.builder()
        .user(user)
        .post(post)
        .build();

    if (postLikeRepository.existsByUserAndPost(user, post)) {
      post.removeLikeFromPost();
      postRepository.save(post);
      PostLike findPostLike = postLikeRepository.findByPostLikeOrThrow(post, user);
      postLikeRepository.delete(findPostLike);
      return Optional.empty();
    }

    post.addLikeToPost();
    postRepository.save(post);
    PostLike savedPostLike = postLikeRepository.save(postLike);
    PostLikeResponseDto postLikeResponseDto = PostLikeResponseDto.builder()
        .postLike(savedPostLike)
        .build();

    return Optional.of(postLikeResponseDto);
  }
}
