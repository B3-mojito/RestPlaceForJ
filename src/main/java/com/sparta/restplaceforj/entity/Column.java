package com.sparta.restplaceforj.entity;

import com.sparta.restplaceforj.dto.ColumnRequestDto;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Date;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

