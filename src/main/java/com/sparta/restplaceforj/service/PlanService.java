package com.sparta.restplaceforj.service;

import com.sparta.restplaceforj.dto.PlanRequestDto;
import com.sparta.restplaceforj.dto.PlanResponseDto;
import com.sparta.restplaceforj.entity.Column;
import com.sparta.restplaceforj.entity.Coworker;
import com.sparta.restplaceforj.entity.Plan;
import com.sparta.restplaceforj.entity.User;
import com.sparta.restplaceforj.exception.CommonException;
import com.sparta.restplaceforj.exception.ErrorEnum;
import com.sparta.restplaceforj.repository.CoworkerRepository;
import com.sparta.restplaceforj.repository.PlanRepository;
import com.sparta.restplaceforj.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
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
   * @param planRequestDto : title
   * @return PlanResponseDto : id, title
   */

  @Transactional
  public PlanResponseDto createPlan(PlanRequestDto planRequestDto, User user) {

    Plan plan = Plan.builder()
        .title(planRequestDto.getTitle())
        .build();
    planRepository.save(plan);

    coworkerRepository.save(Coworker.builder()
        .plan(plan)
        .user(user)
        .build());
    return PlanResponseDto.builder()

        .title(plan.getTitle())
        .build();
  }

  /**
   * 플랜 수정 로직
   *
   * @param planId         플랜 아이디
   * @param planRequestDto : title
   * @return PlanResponseDto : id, title
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
   * @param planId 플랜아이디
   */
  @Transactional
  public void deleteColumn(Long planId, User user) {
    if (!planRepository.existsById(planId)) {
      throw new CommonException(ErrorEnum.PLAN_NOT_FOUND);
    }
    Coworker coworker = coworkerRepository.findByPlanIdOrThrow(planId);
    if (coworker.getUser().getId() != user.getId()) {
      throw new CommonException(ErrorEnum.BAD_REQUEST);
    }
    planRepository.deleteById(planId);
  }


  /**
   * 플랜 다건 조회 로직
   *
   * @param user   유저 디테일 객체
   * @param userId 유저 아이디
   * @return List<PlanResponseDto> : planId, title
   */
  @Transactional
  public List<PlanResponseDto> getPlanList(User user, Long userId) {
    if (!userRepository.existsById(userId)) {
      throw new CommonException(ErrorEnum.USER_NOT_FOUND);
    }
    if (user.getId() != userId) {
      throw new CommonException(ErrorEnum.BAD_REQUEST);
    }
    List<PlanResponseDto> planResponseDtos = new ArrayList<>();
    List<Coworker> coworkers = coworkerRepository.findAllByUserId(userId);

    for (Coworker coworker : coworkers) {
      Plan plans = planRepository.findByIdOrThrow(coworker.getPlan().getId());
      User users = userRepository.findByIdOrThrow(coworker.getUser().getId());

      if (plans == null && users == null) {
        throw new CommonException(ErrorEnum.PLAN_NOT_FOUND);
      }
      PlanResponseDto planResponseDto = PlanResponseDto.builder()
          .id(plans.getId())
          .title(plans.getTitle())
          .build();
      planResponseDtos.add(planResponseDto);
    }
    return planResponseDtos;
  }


  /**
   * 플랜 조회 로직
   *
   * @param planId 플랜 아이디
   * @return PlanResponseDto : planId, title
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
