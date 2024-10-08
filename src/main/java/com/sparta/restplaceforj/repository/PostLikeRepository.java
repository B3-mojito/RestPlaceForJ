package com.sparta.restplaceforj.repository;

import com.sparta.restplaceforj.entity.Post;
import com.sparta.restplaceforj.entity.PostLike;
import com.sparta.restplaceforj.entity.User;
import com.sparta.restplaceforj.exception.CommonException;
import com.sparta.restplaceforj.exception.ErrorEnum;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

  Optional<PostLike> findFirstByPostAndUser(Post post, User user);

  default PostLike findByPostLikeOrThrow(Post post, User user) {
    return findFirstByPostAndUser(post, user).orElseThrow(
        () -> new CommonException(ErrorEnum.POST_LIKE_NOT_FOUND)
    );
  }

  boolean existsByPostAndUser(Post post, User user);
}
