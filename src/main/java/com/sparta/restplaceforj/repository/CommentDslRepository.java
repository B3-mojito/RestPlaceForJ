package com.sparta.restplaceforj.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.restplaceforj.dto.CommentResponseDto;
import com.sparta.restplaceforj.entity.Comment;
import com.sparta.restplaceforj.entity.QComment;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CommentDslRepository {

  private final JPAQueryFactory jpaQueryFactory;
  private final QComment comment = QComment.comment;

  public PageImpl<CommentResponseDto> getCommentList(Pageable pageable, long postId) {
    JPAQuery<Comment> query = jpaQueryFactory.selectFrom(comment)
        .where(comment.post.id.eq(postId))
        .orderBy(comment.id.desc());

    long totalSize = query.fetch().size();
    List<CommentResponseDto> commentList = query.offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch()
        .stream()
        .map(CommentResponseDto::new)
        .toList();

    return new PageImpl<>(commentList, pageable, totalSize);

  }
}
