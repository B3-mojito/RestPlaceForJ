package com.sparta.restplaceforj.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.restplaceforj.entity.QPost;
import com.sparta.restplaceforj.entity.ThemeEnum;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

@Slf4j
@RequiredArgsConstructor
@Repository
public class PostDslRepository {

  private final JPAQueryFactory jpaQueryFactory;

  private QPost post = QPost.post;

  public List<String> getPostListGroupByPlaceName(
      PageRequest pageRequest, String address, ThemeEnum themeEnum) {

    log.info("getPostListGroupByPlaceName called " + pageRequest);

    return jpaQueryFactory.select(post.placeName)
        .from(post)
        .where(post.address.contains(address)
            .and(post.themeEnum.eq(themeEnum)))
        .orderBy(post.count().desc())
        .groupBy(post.placeName)
        .offset(pageRequest.getOffset())
        .limit(pageRequest.getPageSize())
        .fetch();
  }

}
