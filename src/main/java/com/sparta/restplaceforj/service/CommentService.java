package com.sparta.restplaceforj.service;

import com.sparta.restplaceforj.dto.CommentResponseDto;
import com.sparta.restplaceforj.dto.PageResponseDto;
import com.sparta.restplaceforj.entity.Comment;
import com.sparta.restplaceforj.entity.Post;
import com.sparta.restplaceforj.entity.User;
import com.sparta.restplaceforj.exception.CommonException;
import com.sparta.restplaceforj.exception.ErrorEnum;
import com.sparta.restplaceforj.repository.CommentDslRepository;
import com.sparta.restplaceforj.repository.CommentRepository;
import com.sparta.restplaceforj.repository.PostRepository;
import com.sparta.restplaceforj.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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

  /**
   * 댓글 생성
   *
   * @param postId  댓글 작성할 글
   * @param content 작성 내용
   * @param user    작성자
   * @return CommentResponseDto : id, postId, content, likesCount
   */
  @Transactional
  @CachePut(value = "comments", key = "'comments:' + #result.id", cacheManager = "contentCacheManager")
  public CommentResponseDto createPost(long postId, String content, User user) {

    Post post = postRepository.findByIdOrThrow(postId);
    Comment comment = Comment.builder()
        .post(post)
        .user(user)
        .content(content)
        .build();

    commentRepository.save(comment);

    return CommentResponseDto.builder()
        .comment(comment)
        .build();
  }

  /**
   * 댓글 조회
   *
   * @param page   현재 페이지
   * @param size   페이지 사이즈
   * @param postId 조회할 글
   * @return PageResponseDto : contentList, size, page, totalPages, totalElements
   */
  @Cacheable(value = "comments", key = "'comments:' + #postId + ':' + #page + ':' + #size", cacheManager = "contentCacheManager")
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
   * 댓글 수정 .
   *
   * @param commentId 수정할 댓글
   * @param content   : 수정 내용
   * @param userId    작성자만 수정
   * @return : id, postId, content, likesCount
   */
  @Transactional
  @CachePut(value = "comments", key = "'comments:' + #result.id", cacheManager = "contentCacheManager")
  public CommentResponseDto updateComment(long commentId, String content, long userId) {
    Comment comment = commentRepository.findByIdOrThrow(commentId);
    if (comment.getUser().getId() != userId) {
      throw new CommonException(ErrorEnum.COMMENT_MISMATCH);
    }
    comment.updateContent(content);

    return CommentResponseDto.builder()
        .comment(comment)
        .build();
  }

  /**
   * 댓글 삭제 .
   *
   * @param commentId 삭제할 댓글
   * @param userId    작성자
   */
  @Transactional
  @CacheEvict(value = "comments", key = "'comments:' + #commentId", cacheManager = "contentCacheManager")
  public void deleteComment(long commentId, long userId) {
    Comment comment = commentRepository.findByIdOrThrow(commentId);
    if (comment.getUser().getId() != userId) {
      throw new CommonException(ErrorEnum.COMMENT_MISMATCH);
    }

    commentRepository.deleteById(commentId);
  }

}
