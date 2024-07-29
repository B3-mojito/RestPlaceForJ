package com.sparta.restplaceforj.repository;

import com.sparta.restplaceforj.entity.Column;
import com.sparta.restplaceforj.exception.CommonException;
import com.sparta.restplaceforj.exception.ErrorEnum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColumnRepository extends JpaRepository<Column, Long> {

  default Column findByIdOrThrow(Long id) {
    return findById(id).orElseThrow(() -> new CommonException(ErrorEnum.CARD_NOT_FOUND));
  }
}
