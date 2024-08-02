package com.sparta.restplaceforj.repository;

import com.sparta.restplaceforj.dto.ColumnResponseDto;
import com.sparta.restplaceforj.dto.PlanResponseDto;
import com.sparta.restplaceforj.entity.Plan;
import com.sparta.restplaceforj.exception.CommonException;
import com.sparta.restplaceforj.exception.ErrorEnum;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PlanRepository extends JpaRepository<Plan, Long> {

  default Plan findByIdOrThrow(Long id) {
    return findById(id).orElseThrow(() -> new CommonException(ErrorEnum.PLAN_NOT_FOUND));
  }
  
}
