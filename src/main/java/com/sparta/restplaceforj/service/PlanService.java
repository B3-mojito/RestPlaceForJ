package com.sparta.restplaceforj.service;

import com.sparta.restplaceforj.dto.CardResponseDto;
import com.sparta.restplaceforj.dto.PlanRequestDto;
import com.sparta.restplaceforj.dto.PlanResponseDto;
import com.sparta.restplaceforj.entity.Column;
import com.sparta.restplaceforj.entity.Coworker;
import com.sparta.restplaceforj.entity.Plan;
import com.sparta.restplaceforj.entity.User;
import com.sparta.restplaceforj.exception.CommonException;
import com.sparta.restplaceforj.exception.ErrorEnum;
import com.sparta.restplaceforj.repository.*;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PlanService {

  private final PlanRepository planRepository;
  private final ColumnRepository columnRepository;
  private final CoworkerRepository coworkerRepository;
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

    //플랜 생성시 유저를 공동 작업자로 추가
    Coworker coworker = Coworker.builder()
        .plan(plan)
        .user(user)
        .build();

    coworkerRepository.save(coworker);

    //plan 생성시 미정의 컬럼이 생성
    Column column = Column.builder()
        .title("미정")
        .plan(plan)
        .defaultValue(Boolean.TRUE)
        .build();

    columnRepository.save(column);

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
  public PlanResponseDto updatePlan(Long planId, PlanRequestDto planRequestDto, User user) {
    //플랜 가져오기
    Plan plan = planRepository.findByIdOrThrow(planId);

    //유저가 공동작업자로 들어가 있는지 확인
    if (!coworkerRepository.existsByUserIdAndPlanId(user.getId(), planId)) {
      throw new CommonException(ErrorEnum.BAD_REQUEST);
    }

    //플랜 업데이트
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
  public void deletePlan(Long planId, User user) {

    // 존재하는 플랜인지 확인
    Plan plan = planRepository.findByIdOrThrow(planId);

    //유저가 공동작업자로 들어가 있는지 확인
    if (!coworkerRepository.existsByUserIdAndPlanId(user.getId(), planId)) {
      throw new CommonException(ErrorEnum.BAD_REQUEST);
    }

    planRepository.delete(plan);
  }


  /**
   * 플랜 다건 조회 로직
   *
   * @param userId 유저 아이디
   * @return List<PlanResponseDto> : planId, title
   */
  public List<PlanResponseDto> getPlanList(Long userId) {

    // 유저가 존재하는지 확인(마이페이지에서 플랜 조회시 사용)
    if (!coworkerRepository.existsByUserId(userId)) {
      throw new CommonException(ErrorEnum.USER_NOT_FOUND);
    }

    return coworkerRepository.findPlansByUserId(userId);
  }


  /**
   * 플랜 단건 조회 로직
   *
   * @param planId 플랜 아이디
   * @param user   유저 객체
   * @return PlanResponseDto : planId, title
   */
  public PlanResponseDto getPlan(Long planId, User user) {

    // 플랜 가져오기
    Plan plan = planRepository.findByIdOrThrow(planId);

    //유저가 공동작업자로 들어가 있는지 확인
    if (!coworkerRepository.existsByUserIdAndPlanId(user.getId(), planId)) {
      throw new CommonException(ErrorEnum.BAD_REQUEST);
    }

    return PlanResponseDto.builder()
        .id(plan.getId())
        .title(plan.getTitle())
        .build();
  }

  public List<CardResponseDto> getCardLists(Long planId, User user) {
    planRepository.findByIdOrThrow(planId);
    List<Column> columns = columnRepository.findColumnsByPlanId(planId);

    List<Long> columnIds = columns.stream()
        .map(Column::getId)
        .collect(Collectors.toList());

    return cardRepository.findByColumnIdIn(columnIds);
  }
}
