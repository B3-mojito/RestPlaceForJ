package com.sparta.restplaceforj.repository;

import com.sparta.restplaceforj.entity.Comment;
import com.sparta.restplaceforj.exception.CommonException;
import com.sparta.restplaceforj.exception.ErrorEnum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

  default Comment findByIdOrThrow(long commentId) {
    return findById(commentId).orElseThrow(() -> new CommonException(ErrorEnum.COMMENT_NOT_FOUND));
  }
}
