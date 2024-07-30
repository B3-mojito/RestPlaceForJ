package com.sparta.restplaceforj.service;

import com.sparta.restplaceforj.dto.CommentLikeResponseDto;
import com.sparta.restplaceforj.dto.PostLikeResponseDto;
import com.sparta.restplaceforj.entity.Comment;
import com.sparta.restplaceforj.entity.CommentLike;
import com.sparta.restplaceforj.entity.Post;
import com.sparta.restplaceforj.entity.PostLike;
import com.sparta.restplaceforj.entity.User;
import com.sparta.restplaceforj.repository.CommentLikeRepository;
import com.sparta.restplaceforj.repository.CommentRepository;
import com.sparta.restplaceforj.repository.PostLikeRepository;
import com.sparta.restplaceforj.repository.PostRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {

  private final PostLikeRepository postLikeRepository;
  private final PostRepository postRepository;
  private final CommentRepository commentRepository;
  private final CommentLikeRepository commentLikeRepository;

  /**
   * 좋아요 증가&감소.
   */
  public Optional<PostLikeResponseDto> createPostLike(long postId, User user) {
    Post post = postRepository.findByIdOrThrow(postId);
    PostLike postLike = PostLike.builder()
        .user(user)
        .post(post)
        .build();

    if (postLikeRepository.existsByPostAndUser(post, user)) {
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

  /**
   * 댓글 좋아요 증가&감소.
   */
  public Optional<CommentLikeResponseDto> createCommentLike(long commentId, User user) {
    Comment comment = commentRepository.findByIdOrThrow(commentId);
    CommentLike commentLike = CommentLike.builder()
        .user(user)
        .comment(comment)
        .build();

    if (commentLikeRepository.existsByCommentAndUser(comment, user)) {
      comment.removeLikeFromComment();
      commentRepository.save(comment);

      CommentLike findCommentLike = commentLikeRepository.findByCommentLikeOrThrow(comment,
          user);
      commentLikeRepository.delete(findCommentLike);
      return Optional.empty();
    }

    comment.addLikeToComment();
    commentRepository.save(comment);

    CommentLike savedCommentLike = commentLikeRepository.save(commentLike);
    CommentLikeResponseDto commentLikeResponseDto = CommentLikeResponseDto.builder()
        .commentLike(savedCommentLike)
        .build();

    return Optional.of(commentLikeResponseDto);
  }
}
