package com.sparta.restplaceforj.repository;

import com.sparta.restplaceforj.entity.Coworker;
import com.sparta.restplaceforj.entity.Plan;
import com.sparta.restplaceforj.exception.CommonException;
import com.sparta.restplaceforj.exception.ErrorEnum;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoworkerRepository extends JpaRepository<Coworker, Long> {

  List<Coworker> findAllByUserId(Long userId);

  default Coworker findByPlanIdOrThrow(Long planId) {
    return findById(planId).orElseThrow(() -> new CommonException(ErrorEnum.USER_NOT_FOUND));
  }
}
