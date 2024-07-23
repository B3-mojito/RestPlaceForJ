package com.sparta.restplaceforj.controller;

import com.sparta.restplaceforj.common.CommonResponse;
import com.sparta.restplaceforj.common.ResponseEnum;
import com.sparta.restplaceforj.dto.ColumnRequestDto;
import com.sparta.restplaceforj.dto.ColumnResponseDto;
import com.sparta.restplaceforj.entity.Column;
import com.sparta.restplaceforj.service.ColumnService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
     * @param columnRequestDto
     * @return CommonResponse
     */
    @PostMapping
    public ResponseEntity<CommonResponse<ColumnResponseDto>> createColumn(@PathVariable("plan-id") Long planId, @RequestBody ColumnRequestDto columnRequestDto) {
        ColumnResponseDto responseDto = columnService.createColumn(planId, columnRequestDto.getTitle());
        return ResponseEntity.ok(
                CommonResponse.<ColumnResponseDto>builder().response(ResponseEnum.CREATE_COLUMN).data(responseDto).build()
        );
    }
}
