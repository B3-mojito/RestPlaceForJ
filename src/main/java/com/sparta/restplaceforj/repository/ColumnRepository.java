package com.sparta.restplaceforj.repository;

import com.sparta.restplaceforj.dto.ColumnResponseDto;
import com.sparta.restplaceforj.entity.Column;
import com.sparta.restplaceforj.exception.CommonException;
import com.sparta.restplaceforj.exception.ErrorEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ColumnRepository extends JpaRepository<Column, Long> {

    default Column findByIdOrThrow(Long columnId) {
        return findById(columnId).orElseThrow(() -> new CommonException(ErrorEnum.COLUMN_NOT_FOUND));
    }

    List<ColumnResponseDto> findByPlanId(Long planId);
}
