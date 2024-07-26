package com.sparta.restplaceforj.controller;

import com.sparta.restplaceforj.common.CommonResponse;
import com.sparta.restplaceforj.common.ResponseEnum;
import com.sparta.restplaceforj.dto.ColumnRequestDto;
import com.sparta.restplaceforj.dto.ColumnResponseDto;
import com.sparta.restplaceforj.service.ColumnService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/plans/{plan-id}/columns")
public class ColumnController {

  private final ColumnService columnService;

  /**
   * 컬럼 생성 controller
   *
   * @param planId
   * @param requestDto
   * @return CommonResponse
   */
  @PostMapping
  public ResponseEntity<CommonResponse<ColumnResponseDto>> createColumn(
      @PathVariable("plan-id") Long planId,
      @RequestBody ColumnRequestDto requestDto) {
    ColumnResponseDto responseDto = columnService.createColumn(planId, requestDto);
    return ResponseEntity.ok(
        CommonResponse.<ColumnResponseDto>builder()
            .response(ResponseEnum.CREATE_COLUMN)
            .data(responseDto)
            .build()
    );
  }

  /**
   * 컬럼 수정 controller
   *
   * @param planId
   * @param columnId
   * @param requestDto
   * @return CommonResponse
   */
  @PatchMapping("/{column-id}")
  public ResponseEntity<CommonResponse<ColumnResponseDto>> updateColumn(
      @PathVariable("plan-id") Long planId, @PathVariable("column-id") Long columnId,
      @RequestBody ColumnRequestDto requestDto) {
    ColumnResponseDto responseDto = columnService.updateColumn(planId, columnId,
        requestDto);
    return ResponseEntity.ok(
        CommonResponse.<ColumnResponseDto>builder()
            .response(ResponseEnum.UPDATE_COLUMN)
            .data(responseDto)
            .build()
    );
  }

  /**
   * 컬럼 삭제 controller
   *
   * @param planId
   * @param columnId
   * @return CommonResponse
   */
  @DeleteMapping("/{column-id}")
  public ResponseEntity<CommonResponse<ColumnResponseDto>> deleteColumn(
      @PathVariable("plan-id") Long planId, @PathVariable("column-id") Long columnId) {
    columnService.deleteColumn(planId, columnId);
    return ResponseEntity.ok(
        CommonResponse.<ColumnResponseDto>builder()
            .response(ResponseEnum.DELETE_COLUMN)
            .data(null)
            .build()
    );
  }
}
