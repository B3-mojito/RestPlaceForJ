package com.sparta.restplaceforj.repository;

import com.sparta.restplaceforj.dto.ColumnResponseDto;
import com.sparta.restplaceforj.entity.Column;
import com.sparta.restplaceforj.exception.CommonException;
import com.sparta.restplaceforj.exception.ErrorEnum;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColumnRepository extends JpaRepository<Column, Long> {

  default Column findByIdOrThrow(Long columnId) {
    return findById(columnId).orElseThrow(() -> new CommonException(ErrorEnum.COLUMN_NOT_FOUND));
  }

  Column findByPlanIdAndTitle(Long planId, String title) throws CommonException;

  List<ColumnResponseDto> findByPlanId(Long planId);

  List<Column> findColumnsByPlanId(Long planId);

  Column findByPlanIdAndDefaultValue(Long plan_id, Boolean defaultValue);
  

}
