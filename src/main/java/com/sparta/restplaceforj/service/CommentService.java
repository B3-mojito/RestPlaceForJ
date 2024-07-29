package com.sparta.restplaceforj.service;

import com.sparta.restplaceforj.dto.CommentResponseDto;
import com.sparta.restplaceforj.dto.PageResponseDto;
import com.sparta.restplaceforj.entity.Comment;
import com.sparta.restplaceforj.entity.Post;
import com.sparta.restplaceforj.exception.CommonException;
import com.sparta.restplaceforj.exception.ErrorEnum;
import com.sparta.restplaceforj.repository.CommentDslRepository;
import com.sparta.restplaceforj.repository.CommentRepository;
import com.sparta.restplaceforj.repository.PostRepository;
import com.sparta.restplaceforj.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CommentService {

  private final CommentRepository commentRepository;
  private final CommentDslRepository commentDslRepository;
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

  /**
   * 댓글 조회.
   */
  public PageResponseDto<CommentResponseDto> getCommentList(int page, int size, long postId) {

    if (!postRepository.existsById(postId)) {
      throw new CommonException(ErrorEnum.POST_NOT_FOUND);
    }

    Pageable pageRequest = PageRequest.of(page, size);
    PageImpl<CommentResponseDto> commentPage = commentDslRepository
        .getCommentList(pageRequest, postId);

    return PageResponseDto.<CommentResponseDto>builder()
        .page(commentPage)
        .build();
  }

  /**
   * 댓글 수정.
   */
  @Transactional
  public CommentResponseDto updateComment(long commentId, String content) {
    Comment comment = commentRepository.findByIdOrThrow(commentId);
    comment.updateContent(content);

    return CommentResponseDto.builder()
        .comment(comment)
        .build();
  }

}
