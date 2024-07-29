package com.sparta.restplaceforj.service;

import com.sparta.restplaceforj.dto.CommentResponseDto;
import com.sparta.restplaceforj.entity.Comment;
import com.sparta.restplaceforj.entity.Post;
import com.sparta.restplaceforj.repository.CommentRepository;
import com.sparta.restplaceforj.repository.PostRepository;
import com.sparta.restplaceforj.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CommentService {

  private final CommentRepository commentRepository;
  private final PostRepository postRepository;
  private final UserRepository userRepository;

  /**
   * 댓글 생성.
   */
  @Transactional
  public CommentResponseDto createPost(long postId, String content) {

    Post post = postRepository.findByIdOrThrow(postId);
    Comment comment = Comment.builder()
        .post(post)
        .content(content)
        .build();

    commentRepository.save(comment);

    return CommentResponseDto.builder()
        .comment(comment)
        .build();
  }

}
