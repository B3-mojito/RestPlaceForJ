package com.sparta.restplaceforj.entity;

import com.sparta.restplaceforj.dto.ColumnRequestDto;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "columns")
public class Column {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  private LocalDate date;

  private Boolean defaultValue = Boolean.FALSE;

  @OnDelete(action = OnDeleteAction.CASCADE)
  @ManyToOne(fetch = FetchType.LAZY)
  private Plan plan;

  @Builder
  public Column(String title, LocalDate date, Plan plan, Boolean defaultValue) {
    this.title = title;
    this.date = date;
    this.plan = plan;
    this.defaultValue = defaultValue;
  }

  public void updateColumn(ColumnRequestDto columnRequestDto) {
    this.title = columnRequestDto.getTitle();
    this.date = columnRequestDto.getDate();
  }
}

