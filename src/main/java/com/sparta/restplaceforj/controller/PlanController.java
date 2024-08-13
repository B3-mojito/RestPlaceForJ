package com.sparta.restplaceforj.controller;

import com.sparta.restplaceforj.common.CommonResponse;
import com.sparta.restplaceforj.common.ResponseEnum;
import com.sparta.restplaceforj.dto.CardResponseDto;
import com.sparta.restplaceforj.dto.ColumnResponseDto;
import com.sparta.restplaceforj.dto.PlanRequestDto;
import com.sparta.restplaceforj.dto.PlanResponseDto;
import com.sparta.restplaceforj.security.UserDetailsImpl;
import com.sparta.restplaceforj.service.PlanService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/plans")
public class PlanController {

  private final PlanService planService;

  /**
   * 플랜 생성 controller
   *
   * @param planRequestDto : title
   * @return PlanResponseDto : id, title
   */
  @PostMapping
  public ResponseEntity<CommonResponse<PlanResponseDto>> createPlan(
      @RequestBody @Valid PlanRequestDto planRequestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    PlanResponseDto responseDto = planService.createPlan(planRequestDto, userDetails.getUser());
    return ResponseEntity.ok(
        CommonResponse.<PlanResponseDto>builder()
            .response(ResponseEnum.CREATE_PLAN)
            .data(responseDto)
            .build()
    );
  }

  /**
   * 플랜 수정 controller
   *
   * @param planId         플랜아이디
   * @param planRequestDto : title
   * @return PlanResponseDto : id, title
   */
  @PatchMapping("/{plan-id}")
  public ResponseEntity<CommonResponse<PlanResponseDto>> updatePlan(
      @PathVariable("plan-id") Long planId,
      @RequestBody @Valid PlanRequestDto planRequestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    PlanResponseDto planResponseDto = planService
        .updatePlan(planId, planRequestDto, userDetails.getUser());

    return ResponseEntity.ok(
        CommonResponse.<PlanResponseDto>builder()
            .response(ResponseEnum.UPDATE_PLAN)
            .data(planResponseDto)
            .build()
    );
  }

  /**
   * 플랜 삭제 controller
   *
   * @param planId 플랜 아이디
   * @return CommonResponse : null
   */
  @DeleteMapping("/{plan-id}")
  public ResponseEntity<CommonResponse<PlanResponseDto>> deletePlan(
      @PathVariable("plan-id") Long planId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {

    planService.deletePlan(planId, userDetails.getUser());

    return ResponseEntity.ok(
        CommonResponse.<PlanResponseDto>builder()
            .response(ResponseEnum.DELETE_PLAN)
            .data(null)
            .build()
    );
  }


  /**
   * 플랜 다건 조회 controller
   *
   * @param userId 유저 아이디
   * @return PlanResponseDto : id, title
   */
  @GetMapping
  public ResponseEntity<CommonResponse<List<PlanResponseDto>>> getPlanList(
      @RequestParam Long userId) {
    List<PlanResponseDto> planResponseDtoList = planService
        .getPlanList(userId);

    return ResponseEntity.ok(
        CommonResponse.<List<PlanResponseDto>>builder()
            .response(ResponseEnum.GET_PLAN_LIST)
            .data(planResponseDtoList)
            .build()
    );
  }

  /**
   * 플랜 본인 다건 조회 controller
   *
   * @param userDetails 유저 아이디
   * @return PlanResponseDto : id, title
   */
  @GetMapping("/myPlans")
  public ResponseEntity<CommonResponse<List<PlanResponseDto>>> getMyPlanList(
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    List<PlanResponseDto> planResponseDtoList = planService
        .getPlanList(userDetails.getUser().getId());

    return ResponseEntity.ok(
        CommonResponse.<List<PlanResponseDto>>builder()
            .response(ResponseEnum.GET_PLAN_LIST)
            .data(planResponseDtoList)
            .build()
    );
  }

  /**
   * 플랜 단건 조회 controller
   *
   * @param planId      플랜 아이디
   * @param userDetails 유저 디테일
   * @return PlanResponseDto : id, title
   */
  @GetMapping("{plan-id}")
  public ResponseEntity<CommonResponse<PlanResponseDto>> getPlan(
      @PathVariable("plan-id") Long planId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    PlanResponseDto planResponseDto = planService
        .getPlan(planId, userDetails.getUser());

    return ResponseEntity.ok(
        CommonResponse.<PlanResponseDto>builder()
            .response(ResponseEnum.GET_PLAN)
            .data(planResponseDto)
            .build()
    );
  }

  /**
   * 플랜 으로 카드 전건 조회 controller
   *
   * @param planId      플랜 아이디
   * @param userDetails 유저 디테일
   * @return PlanResponseDto : id, title
   */
  @GetMapping("/{plan-id}/cards")
  public ResponseEntity<CommonResponse<List<CardResponseDto>>> getCardLists(
      @PathVariable("plan-id") Long planId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    List<CardResponseDto> cardResponseDto = planService
        .getCardLists(planId, userDetails.getUser());

    return ResponseEntity.ok(
        CommonResponse.<List<CardResponseDto>>builder()
            .response(ResponseEnum.GET_PLAN)
            .data(cardResponseDto)
            .build()
    );
  }
}