package com.sparta.restplaceforj.repository;


import com.sparta.restplaceforj.dto.PostResponseDto;
import com.sparta.restplaceforj.entity.RelatedPost;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RelatedPostRepository extends JpaRepository<RelatedPost, Long> {

  @Query("SELECT distinct new com.sparta.restplaceforj.dto.PostResponseDto(p) " +
      "FROM RelatedPost c JOIN c.post p WHERE c.card.id = :cardId")
  List<PostResponseDto> findPostsByCardId(Long cardId);
}
