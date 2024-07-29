package com.sparta.restplaceforj.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Entity
@Table(name = "cards")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class Card {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  private String address;

  @OnDelete(action = OnDeleteAction.CASCADE)
  @ManyToOne(fetch = FetchType.LAZY)
  private Column column;

  private String placeName;

  private LocalTime startedAt;

  private LocalTime endedAt;

  private String memo;


  @Builder
  public Card(String title, String address, Column column, String placeName,
      LocalTime startedAt, LocalTime endedAt, String memo) {
    this.title = title;
    this.address = address;
    this.column = column;
    this.placeName = placeName;
    this.startedAt = startedAt;
    this.endedAt = endedAt;
    this.memo = memo;
  }
}
