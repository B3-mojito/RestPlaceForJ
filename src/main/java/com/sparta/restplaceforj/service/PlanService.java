package com.sparta.restplaceforj.service;

import com.sparta.restplaceforj.dto.PlanRequestDto;
import com.sparta.restplaceforj.dto.PlanResponseDto;
import com.sparta.restplaceforj.entity.Column;
import com.sparta.restplaceforj.entity.Plan;
import com.sparta.restplaceforj.exception.CommonException;
import com.sparta.restplaceforj.exception.ErrorEnum;
import com.sparta.restplaceforj.repository.CoworkerRepository;
import com.sparta.restplaceforj.repository.PlanRepository;
import com.sparta.restplaceforj.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PlanService {

  private final PlanRepository planRepository;
  private final CoworkerRepository coworkerRepository;
  private final UserRepository userRepository;

  /**
   * 플랜 생성 메서드
   *
   * @param planRequestDto
   * @return PlanResponseDto
   */

  @Transactional
  public PlanResponseDto createPlan(PlanRequestDto planRequestDto) {

    Plan plan = Plan.builder()
        .title(planRequestDto.getTitle())
        .build();
    planRepository.save(plan);

      /*
       * 추후 유저 완성시 사용
       coworkerRepository.save(Coworker.builder()
       .plan(plan)
       .user()
       .build());
      */
    return PlanResponseDto.builder()

        .title(plan.getTitle())
        .build();
  }

  /**
   * 플랜 수정 로직
   *
   * @param planId
   * @param planRequestDto
   * @return ColumnResponseDto
   */
  @Transactional
  public PlanResponseDto updateColumn(Long planId, PlanRequestDto planRequestDto) {

    Plan plan = planRepository.findByIdOrThrow(planId);

    plan.updatePlan(planRequestDto);

    return PlanResponseDto.builder()
        .id(plan.getId())
        .title(plan.getTitle())
        .build();
  }

  /**
   * 플랜 삭제 로직
   *
   * @param planId
   */
  @Transactional
  public void deleteColumn(Long planId) {
    if (!planRepository.existsById(planId)) {
      throw new CommonException(ErrorEnum.PLAN_NOT_FOUND);
    }
    planRepository.deleteById(planId);
  }

/*
  /**
   * 컬럼 다건 조회 로직
   *
   * @param planId

  @Transactional
  public List<PlanResponseDto> getPlanList(Long planId) {
    if (!planRepository.existsById(planId)) {
      throw new CommonException(ErrorEnum.PLAN_NOT_FOUND);
    }
    return coworkerRepository.findAllByUserId(planId);
  }
*/


  /**
   * 플랜 조회 로직
   *
   * @param planId
   * @return ColumnResponseDto
   */
  @Transactional
  public PlanResponseDto getPlan(Long planId) {

    Plan plan = planRepository.findByIdOrThrow(planId);

    return PlanResponseDto.builder()
        .id(plan.getId())
        .title(plan.getTitle())
        .build();
  }
}
