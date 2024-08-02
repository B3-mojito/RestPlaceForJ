package com.sparta.restplaceforj.service;

import com.sparta.restplaceforj.dto.CardResponseDto;
import com.sparta.restplaceforj.dto.PlanListDto;
import com.sparta.restplaceforj.dto.PlanRequestDto;
import com.sparta.restplaceforj.dto.PlanResponseDto;
import com.sparta.restplaceforj.entity.Column;
import com.sparta.restplaceforj.entity.Coworker;
import com.sparta.restplaceforj.entity.Plan;
import com.sparta.restplaceforj.entity.User;
import com.sparta.restplaceforj.exception.CommonException;
import com.sparta.restplaceforj.exception.ErrorEnum;
import com.sparta.restplaceforj.repository.CardRepository;
import com.sparta.restplaceforj.repository.ColumnRepository;
import com.sparta.restplaceforj.repository.CoworkerRepository;
import com.sparta.restplaceforj.repository.PlanRepository;
import com.sparta.restplaceforj.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Columns;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PlanService {

  private final PlanRepository planRepository;
  private final CoworkerRepository coworkerRepository;
  private final UserRepository userRepository;
  private final ColumnRepository columnRepository;
  private final CardRepository cardRepository;

  /**
   * 플랜 생성 메서드
   *
   * @param planRequestDto : title
   * @return PlanResponseDto : id, title
   */

  @Transactional
  public PlanResponseDto createPlan(PlanRequestDto planRequestDto, User user) {
    Plan plan = Plan.builder().title(planRequestDto.getTitle()).build();
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
   * @param user           유저 디테일즈
   * @return PlanResponseDto : id, title
   */
  @Transactional
  public PlanResponseDto updateColumn(Long planId, PlanRequestDto planRequestDto, User user) {

    Plan plan = planRepository.findByIdOrThrow(planId);
    Coworker coworker = coworkerRepository.findByPlanIdOrThrow(planId);
    if (coworker.getUser().getId() != user.getId()) {
      throw new CommonException(ErrorEnum.BAD_REQUEST);
    }
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
   * @param user   유저 객체
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
   * @param userId 유저 아이디
   * @return List<PlanResponseDto> : planId, title
   */
  public List<PlanResponseDto> getPlanList(Long userId) {
    if (!userRepository.existsById(userId)) {
      throw new CommonException(ErrorEnum.USER_NOT_FOUND);
    }

    return coworkerRepository.findPlansByUserId(userId);
  }


  /**
   * 플랜 조회 로직
   *
   * @param planId 플랜 아이디
   * @param user   유저 객체
   * @return PlanResponseDto : planId, title
   */
  public PlanResponseDto getPlan(Long planId, User user) {

    Plan plan = planRepository.findByIdOrThrow(planId);
    Coworker coworker = coworkerRepository.findByPlanIdOrThrow(planId);
    if (user.getId() != coworker.getUser().getId()) {
      throw new CommonException(ErrorEnum.BAD_REQUEST);
    }
    return PlanResponseDto.builder()
        .id(plan.getId())
        .title(plan.getTitle())
        .build();
  }

  public List<CardResponseDto> getCardLists(Long planId, User user) {
    Plan plan = planRepository.findByIdOrThrow(planId);
    List<Column> columns = columnRepository.findColumnsByPlanId(planId);

    List<Long> columnIds = columns.stream()
        .map(Column::getId)
        .collect(Collectors.toList());

    return cardRepository.findByColumnIdIn(columnIds);
  }
}
