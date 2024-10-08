package com.sparta.restplaceforj.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.restplaceforj.dto.CardUpdateRequestDto;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
  @JoinColumn(name = "column_id")
  private Column column;

  private String placeName;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  private LocalTime startedAt;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  private LocalTime endedAt;

  private String memo;

  public void changeColumn(Column column) {
    this.column = column;
  }

  public void updateCard(CardUpdateRequestDto cardUpdateRequestDto) {
    if (cardUpdateRequestDto.getTitle() != null) {
      this.title = cardUpdateRequestDto.getTitle();
    }
    if (cardUpdateRequestDto.getAddress() != null) {
      this.address = cardUpdateRequestDto.getAddress();
    }
    if (cardUpdateRequestDto.getPlaceName() != null) {
      this.placeName = cardUpdateRequestDto.getPlaceName();
    }
    if (cardUpdateRequestDto.getStartedAt() != null) {
      this.startedAt = cardUpdateRequestDto.getStartedAt();
    }
    if (cardUpdateRequestDto.getEndedAt() != null) {
      this.endedAt = cardUpdateRequestDto.getEndedAt();
    }
    if (cardUpdateRequestDto.getMemo() != null) {
      this.memo = cardUpdateRequestDto.getMemo();
    }
  }

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
