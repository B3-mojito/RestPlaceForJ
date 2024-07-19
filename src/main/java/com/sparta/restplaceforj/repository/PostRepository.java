package com.sparta.restplaceforj.repository;

import com.sparta.restplaceforj.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
