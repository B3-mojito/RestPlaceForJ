package com.sparta.restplaceforj.repository;

import com.sparta.restplaceforj.entity.Plan;
import com.sparta.restplaceforj.exception.CommonException;
import com.sparta.restplaceforj.exception.ErrorEnum;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PlanRepository extends JpaRepository<Plan, Long> {
    default Plan findPlanById(Long id){
        return findById(id).orElseThrow(()-> new CommonException(ErrorEnum.PLAN_NOT_FOUND));
    }
}
