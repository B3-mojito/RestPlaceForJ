package com.sparta.restplaceforj.repository;

import com.sparta.restplaceforj.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
