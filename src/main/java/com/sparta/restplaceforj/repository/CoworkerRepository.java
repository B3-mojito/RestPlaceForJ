package com.sparta.restplaceforj.repository;

import com.sparta.restplaceforj.entity.Coworker;
import com.sparta.restplaceforj.entity.Plan;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoworkerRepository extends JpaRepository<Coworker, Long> {

  List<Plan> findAllByUserId(Long userId);

  boolean existsByUserIdAndPlanId(Long id, Long planId);
}
