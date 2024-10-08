package com.sparta.restplaceforj.repository;

import com.sparta.restplaceforj.dto.PlanListDto;
import com.sparta.restplaceforj.dto.PlanResponseDto;
import com.sparta.restplaceforj.dto.UserImageResponseDto;
import com.sparta.restplaceforj.entity.Coworker;
import com.sparta.restplaceforj.entity.Plan;
import com.sparta.restplaceforj.entity.User;
import com.sparta.restplaceforj.exception.CommonException;
import com.sparta.restplaceforj.exception.ErrorEnum;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CoworkerRepository extends JpaRepository<Coworker, Long> {

  @Query("SELECT new com.sparta.restplaceforj.dto.PlanResponseDto(p.id, p.title) " +
      "FROM Coworker c JOIN c.plan p WHERE c.user.id = :userId")
  List<PlanResponseDto> findPlansByUserId(Long userId);

  default Coworker findByPlanIdOrThrow(Long planId) {
    return findById(planId).orElseThrow(() -> new CommonException(ErrorEnum.USER_NOT_FOUND));
  }

  @Query(
      "SELECT new com.sparta.restplaceforj.dto.UserImageResponseDto(c.user.id, c.user.profileImage, c.user.nickname) "
          +
          "FROM Coworker c WHERE c.plan.id = :planId")
  List<UserImageResponseDto> findUsersByPlanId(Long planId);

  boolean existsByUserIdAndPlanId(Long userId, Long planId);

  boolean existsByUserId(Long userId);
}
