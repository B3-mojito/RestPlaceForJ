package com.sparta.restplaceforj.controller;

import com.sparta.restplaceforj.common.CommonResponse;
import com.sparta.restplaceforj.common.ResponseEnum;
import com.sparta.restplaceforj.dto.CardRequestDto;
import com.sparta.restplaceforj.dto.CardResponseDto;
import com.sparta.restplaceforj.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/cards")
public class CardController {
    private final CardService cardService;

    /**
     * 카드 생성 controller
     *
     * @param postId
     * @param columnId
     * @param cardRequestDto
     * @return CommonResponse
     */
    @PostMapping
    public ResponseEntity<CommonResponse<CardResponseDto>> createCard(
            @RequestParam(value = "post-id") Long postId,
            @RequestParam(value = "column-id") Long columnId,
            @RequestBody CardRequestDto cardRequestDto) {

        CardResponseDto responseDto = cardService.createCard(
                postId,
                columnId,
                cardRequestDto.getTitle(),
                cardRequestDto.getAddress(),
                cardRequestDto.getTime()
        );

        return ResponseEntity.ok(
                CommonResponse.<CardResponseDto>builder()
                        .response(ResponseEnum.CREATE_CARD)
                        .data(responseDto)
                        .build()
        );
    }

    /**
     * 카드 생성 controller
     *
     * @param cardId
     * @param cardRequestDto
     * @return CommonResponse
     */
    @PatchMapping("/{cardId}")
    public ResponseEntity<CommonResponse<CardResponseDto>> updateCard(
            @RequestParam(value = "card-id") Long cardId,
            @RequestBody CardRequestDto cardRequestDto){

        CardResponseDto responseDto = cardService.updateCard(
                cardId,
                cardRequestDto.getTitle(),
                cardRequestDto.getAddress(),
                cardRequestDto.getTime()
        );

        return ResponseEntity.ok(
                CommonResponse.<CardResponseDto>builder()
                        .response(ResponseEnum.UPDATE_CARD)
                        .data(responseDto)
                        .build()
        );
    }





}

