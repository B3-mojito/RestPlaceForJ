package com.sparta.restplaceforj.entity;

import com.sparta.restplaceforj.dto.PostRequestDto;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "posts")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @ManyToOne(fetch = FetchType.LAZY)
  private User user;

  private String title;

  private String content;

  private String address;

  private long like_count;

  private long view_count;

  @Enumerated(EnumType.STRING)
  private ThemaEnum themeEnum;

  @Builder
  public Post(PostRequestDto requestDto, User user) {
    this.user = user;
    this.title = requestDto.getTitle();
    this.content = requestDto.getContent();
    this.address = requestDto.getAddress();
    this.themeEnum = requestDto.getThemaEnum();
  }
}
