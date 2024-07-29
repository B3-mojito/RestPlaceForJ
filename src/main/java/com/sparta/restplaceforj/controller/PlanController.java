package com.sparta.restplaceforj.controller;

import com.sparta.restplaceforj.common.CommonResponse;
import com.sparta.restplaceforj.common.ResponseEnum;
import com.sparta.restplaceforj.dto.ColumnResponseDto;
import com.sparta.restplaceforj.dto.PlanRequestDto;
import com.sparta.restplaceforj.dto.PlanResponseDto;
import com.sparta.restplaceforj.service.PlanService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/plans")
public class PlanController {

  private final PlanService planService;

  /**
   * 플랜 생성 controller
   *
   * @param planRequestDto
   * @return CommonResponse
   */
  @PostMapping
  public ResponseEntity<CommonResponse<PlanResponseDto>> createPlan(
      @RequestBody @Valid PlanRequestDto planRequestDto) {
    PlanResponseDto responseDto = planService.createPlan(planRequestDto);
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
   * @param planId
   * @param planRequestDto
   * @return CommonResponse
   */
  @PatchMapping("/{plan-id}")
  public ResponseEntity<CommonResponse<PlanResponseDto>> updateColumn(
      @PathVariable("plan-id") Long planId,
      @RequestBody @Valid PlanRequestDto planRequestDto) {
    PlanResponseDto planResponseDto = planService
        .updateColumn(planId, planRequestDto);

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
   * @param planId
   * @return CommonResponse
   */
  @DeleteMapping("/{plan-id}")
  public ResponseEntity<CommonResponse<ColumnResponseDto>> deletePlan(
      @PathVariable("plan-id") Long planId) {
    planService.deleteColumn(planId);

    return ResponseEntity.ok(
        CommonResponse.<ColumnResponseDto>builder()
            .response(ResponseEnum.DELETE_PLAN)
            .data(null)
            .build()
    );
  }

/*
  /**
   * 플랜 다건 조회 controller
   *
   * @param planId
   * @return CommonResponse

  @GetMapping("{user-id}")
  public ResponseEntity<CommonResponse<List<PlanResponseDto>>> getPlanList(
      @RequestParam(value = "user-id") Long userId) {
    List<PlanResponseDto> planResponseDto = planService
        .getPlanList(planId);

    return ResponseEntity.ok(
        CommonResponse.<List<PlanResponseDto>>builder()
            .response(ResponseEnum.GET_PLAN_LIST)
            .data(planResponseDto)
            .build()
    );
  }
*/

  /**
   * 플랜 조회 controller
   *
   * @param planId
   * @return CommonResponse
   */
  @GetMapping("{plan-id}")
  public ResponseEntity<CommonResponse<PlanResponseDto>> getPlan(
      @PathVariable("plan-id") Long planId) {
    PlanResponseDto planResponseDto = planService
        .getPlan(planId);

    return ResponseEntity.ok(
        CommonResponse.<PlanResponseDto>builder()
            .response(ResponseEnum.GET_PLAN)
            .data(planResponseDto)
            .build()
    );
  }
}