package com.sparta.restplaceforj.controller;

import com.sparta.restplaceforj.common.CommonResponse;
import com.sparta.restplaceforj.common.ResponseEnum;
import com.sparta.restplaceforj.dto.ColumnRequestDto;
import com.sparta.restplaceforj.dto.ColumnResponseDto;
import com.sparta.restplaceforj.entity.Column;
import com.sparta.restplaceforj.service.ColumnService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/columns")
public class ColumnController {
    private final ColumnService columnService;

    /**
     * 컬럼 생성 controller
     *
     * @param planId
     * @param columnRequestDto
     * @return CommonResponse
     */
    @PostMapping
    public ResponseEntity<CommonResponse<ColumnResponseDto>> createColumn(@RequestParam(value = "plan-id") Long planId, @RequestBody ColumnRequestDto columnRequestDto) {
        ColumnResponseDto responseDto = columnService.createColumn(planId, columnRequestDto.getTitle());
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
     * @param columnRequestDto
     * @return CommonResponse
     */
    @PatchMapping("/{column-id}")
    public ResponseEntity<CommonResponse<ColumnResponseDto>> updateColumn(@RequestParam(value = "plan-id") Long planId,@PathVariable("column-id") Long columnId, @RequestBody ColumnRequestDto columnRequestDto) {
        ColumnResponseDto responseDto = columnService.updateColumn(planId,columnId ,columnRequestDto.getTitle());
        return ResponseEntity.ok(
                CommonResponse.<ColumnResponseDto>builder()
                        .response(ResponseEnum.UPDATE_COLUMN)
                        .data(responseDto)
                        .build()
        );
    }
}
