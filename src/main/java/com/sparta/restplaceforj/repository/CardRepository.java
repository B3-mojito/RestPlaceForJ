package com.sparta.restplaceforj.repository;

import com.sparta.restplaceforj.dto.CardResponseDto;
import com.sparta.restplaceforj.entity.Card;
import com.sparta.restplaceforj.entity.Column;
import com.sparta.restplaceforj.exception.CommonException;
import com.sparta.restplaceforj.exception.ErrorEnum;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {

  default Card findByIdOrThrow(Long id) {
    return findById(id).orElseThrow(() -> new CommonException(ErrorEnum.CARD_NOT_FOUND));
  }

  List<CardResponseDto> findAllByColumn(Column column);

  List<Card> findByColumnId(Long columnId);

  List<CardResponseDto> findByColumnIdIn(List<Long> columnIds);
}
