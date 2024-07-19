package com.sparta.restplaceforj.repository;

import com.sparta.restplaceforj.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan, Long> {
}
