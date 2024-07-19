package com.sparta.restplaceforj.repository;

import com.sparta.restplaceforj.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
}
