package com.sparta.restplaceforj.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Getter
@Entity
@Table(name = "cards")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Card {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  private String address;

  private LocalDateTime time;

  @OnDelete(action = OnDeleteAction.CASCADE)
  @ManyToOne(fetch = FetchType.LAZY)
  private Post post;

  @OnDelete(action = OnDeleteAction.CASCADE)
  @ManyToOne(fetch = FetchType.LAZY)
  private Column column;

  @Builder
  public Card(String title, String address, LocalDateTime time, Column column, Post post) {
    this.title = title;
    this.address = address;
    this.time = time;
    this.column = column;
    this.post = post;
  }

}
