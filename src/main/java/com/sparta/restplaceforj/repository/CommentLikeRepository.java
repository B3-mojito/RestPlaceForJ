package com.sparta.restplaceforj.repository;

import com.sparta.restplaceforj.entity.Comment;
import com.sparta.restplaceforj.entity.CommentLike;
import com.sparta.restplaceforj.entity.User;
import com.sparta.restplaceforj.exception.CommonException;
import com.sparta.restplaceforj.exception.ErrorEnum;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

  boolean existsByCommentAndUser(Comment comment, User user);

  Optional<CommentLike> findByCommentAndUser(Comment comment, User user);

  default CommentLike findByCommentLikeOrThrow(Comment comment, User user) {
    return findByCommentAndUser(comment, user).orElseThrow(
        () -> new CommonException(ErrorEnum.COMMENT_LIKE_NOT_FOUND)
    );
  }

}
