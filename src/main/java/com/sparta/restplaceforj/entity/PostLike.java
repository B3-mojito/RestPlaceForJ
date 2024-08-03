package com.sparta.restplaceforj.entity;

import jakarta.persistence.Entity;
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
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Entity
@Table(name = "post_likes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLike {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @OnDelete(action = OnDeleteAction.CASCADE)
  @ManyToOne(fetch = FetchType.LAZY)
  private User user;

  @OnDelete(action = OnDeleteAction.CASCADE)
  @ManyToOne(fetch = FetchType.LAZY)
  private Post post;

  @Builder
  public PostLike(User user, Post post) {
    this.user = user;
    this.post = post;
  }
}
