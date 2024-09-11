package com.sparta.restplaceforj.repository;

import com.sparta.restplaceforj.entity.Post;
import com.sparta.restplaceforj.exception.CommonException;
import com.sparta.restplaceforj.exception.ErrorEnum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

  default Post findByIdOrThrow(long postId) {
    return findById(postId).orElseThrow(
        () -> new CommonException(ErrorEnum.POST_NOT_FOUND)
    );
  }
}
