package com.sparta.restplaceforj.repository;

import com.sparta.restplaceforj.entity.Plan;
import com.sparta.restplaceforj.exception.CommonException;
import com.sparta.restplaceforj.exception.ErrorEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {


    default Plan findPlanById(Long id){
        return findById(id).orElseThrow(()-> new CommonException(ErrorEnum.COLUMN_NOT_FOUND));
    }
}
