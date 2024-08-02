package com.sparta.restplaceforj.repository;


import com.sparta.restplaceforj.dto.PostResponseDto;
import com.sparta.restplaceforj.entity.CardPost;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CardPostRepository extends JpaRepository<CardPost, Long> {

  @Query("SELECT new com.sparta.restplaceforj.dto.PostResponseDto(p) " +
      "FROM CardPost c JOIN c.post p WHERE c.card.id = :cardId")
  List<PostResponseDto> findPostsByCardId(Long cardId);
}
