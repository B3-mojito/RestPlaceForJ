package com.sparta.restplaceforj.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.restplaceforj.dto.PostIdTitleDto;
import com.sparta.restplaceforj.entity.Post;
import com.sparta.restplaceforj.entity.QPost;
import com.sparta.restplaceforj.entity.ThemeEnum;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PostDslRepository {

  private final JPAQueryFactory jpaQueryFactory;
  private final QPost post = QPost.post;

  public PageImpl<String> getPostListGroupByPlaceName(
      Pageable pageable, String shortAddress, ThemeEnum themeEnum) {

    JPQLQuery<String> query = jpaQueryFactory.select(post.placeName)
        .from(post)
        .where(post.address.contains(shortAddress)
            .and(post.themeEnum.eq(themeEnum)))
        .orderBy(post.count().desc())
        .groupBy(post.placeName);

    long totalSize = query.fetch().size();
    List<String> placeNameList = query.offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    return new PageImpl<>(placeNameList, pageable, totalSize);
  }

  public PageImpl<PostIdTitleDto> getPostTitleList(Pageable pageable, String placeName, String q) {

    OrderSpecifier<?> orderSpecifier = getOrderSpecifier(pageable.getSort(), post);

    JPAQuery<PostIdTitleDto> query;

    if (q == null) {
      query = jpaQueryFactory
          .select(Projections.fields(PostIdTitleDto.class, post.id, post.title))
          .from(post)
          .where(post.placeName.eq(placeName));
    } else {
      query = jpaQueryFactory
          .select(Projections.fields(PostIdTitleDto.class, post.id, post.title))
          .from(post)
          .where(post.placeName.eq(placeName).and(post.title.contains(q)));
    }

    long totalSize = query.fetchCount();

    List<PostIdTitleDto> postIdAndTitleDtoList = query.offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    return new PageImpl<>(postIdAndTitleDtoList, pageable, totalSize);

  }

  private OrderSpecifier<?> getOrderSpecifier(Sort sort, QPost post) {
    for (Sort.Order order : sort) {
      PathBuilder<Post> pathBuilder = new PathBuilder<>(post.getType(), post.getMetadata());
      return new OrderSpecifier(
          order.isAscending() ? com.querydsl.core.types.Order.ASC :
              com.querydsl.core.types.Order.DESC, pathBuilder.get(order.getProperty())
      );
    }
    return null;
  }
}
