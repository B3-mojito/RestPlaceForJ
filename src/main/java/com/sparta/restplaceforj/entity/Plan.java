package com.sparta.restplaceforj.entity;

import com.sparta.restplaceforj.dto.PlanRequestDto;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "plans")
public class Plan {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<Column> columns = new ArrayList<>();

  @Builder
  public Plan(String title) {
    this.title = title;
  }

  public void updatePlan(PlanRequestDto planRequestDto) {
    this.title = planRequestDto.getTitle();
  }
}