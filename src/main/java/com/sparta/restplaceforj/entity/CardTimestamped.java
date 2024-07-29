package com.sparta.restplaceforj.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class CardTimestamped {

  @CreatedDate
  @Column(updatable = true, nullable = false)
  @Temporal(TemporalType.DATE)
  protected LocalDateTime startedAt;

  @CreatedDate
  @Column
  @Temporal(TemporalType.DATE)
  protected LocalDateTime endedAt;
}
