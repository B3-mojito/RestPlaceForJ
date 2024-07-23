package com.sparta.restplaceforj.repository;

import com.sparta.restplaceforj.entity.Card;
import com.sparta.restplaceforj.entity.Column;
import com.sparta.restplaceforj.entity.Plan;
import com.sparta.restplaceforj.exception.CommonException;
import com.sparta.restplaceforj.exception.ErrorEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ColumnRepository extends JpaRepository<Column, Long> {
    default Column findColumnById(Long id) {
        return findById(id).orElseThrow(() -> new CommonException(ErrorEnum.CARD_NOT_FOUND));
    }
}
