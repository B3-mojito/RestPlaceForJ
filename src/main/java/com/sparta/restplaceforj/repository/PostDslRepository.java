package com.sparta.restplaceforj.repository;

import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.restplaceforj.entity.QPost;
import com.sparta.restplaceforj.entity.ThemeEnum;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Slf4j
@RequiredArgsConstructor
@Repository
public class PostDslRepository {

  private final JPAQueryFactory jpaQueryFactory;
  private final QPost post = QPost.post;

  public PageImpl<String> getPostListGroupByPlaceName(
      Pageable pageable, String shortAddress, ThemeEnum themeEnum) {

    log.info("getPostListGroupByPlaceName called " + pageable);

    JPQLQuery<String> query = jpaQueryFactory.select(post.placeName)
        .from(post)
        .where(post.address.contains(shortAddress)
            .and(post.themeEnum.eq(themeEnum)))
        .orderBy(post.count().desc())
        .groupBy(post.placeName)
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize());

    long totalSize = query.fetchCount();
    List<String> placeNameList = query.offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    return new PageImpl<>(placeNameList, pageable, totalSize);
  }

}
