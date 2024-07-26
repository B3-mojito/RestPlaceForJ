package com.sparta.restplaceforj.controller;

import com.sparta.restplaceforj.common.CommonResponse;
import com.sparta.restplaceforj.common.ResponseEnum;
import com.sparta.restplaceforj.dto.CardRequestDto;
import com.sparta.restplaceforj.dto.CardResponseDto;
import com.sparta.restplaceforj.dto.CardUpdateRequestDto;
import com.sparta.restplaceforj.repository.CardRepository;
import com.sparta.restplaceforj.service.CardService;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/columns/{column-id}/cards")
public class CardController {

  private final CardService cardService;

  /**
   * 카드 생성 controller
   *
   * @param columnId
   * @param cardRequestDto
   * @return CommonResponse
   */
  @PostMapping("")
  public ResponseEntity<CommonResponse<CardResponseDto>> createCard(

      @RequestParam(value = "column-id") Long columnId,
      @RequestBody CardRequestDto cardRequestDto) {

    CardResponseDto responseDto = cardService.createCard(
        columnId,
        cardRequestDto.getTitle(),
        cardRequestDto.getAddress(),
        cardRequestDto.getPlaceName(),
        cardRequestDto.getMemo()
    );

    return ResponseEntity.ok(
        CommonResponse.<CardResponseDto>builder()
            .response(ResponseEnum.CREATE_CARD)
            .data(responseDto)
            .build()
    );
  }

  /**
   * 카드 수정 controller
   *
   * @param cardId
   * @param cardUpdateRequestDto
   * @return CommonResponse
   */
  @PatchMapping("/{card-id}")
  public ResponseEntity<CommonResponse<CardResponseDto>> updateCard(
      @RequestParam(value = "card-id") Long cardId,
      @RequestBody CardUpdateRequestDto cardUpdateRequestDto) {

    return ResponseEntity.ok(
        CommonResponse.<CardResponseDto>builder()
            .response(ResponseEnum.UPDATE_CARD)
            .data(cardService.updateCard(cardId, cardUpdateRequestDto))
            .build()
    );
  }

  /**
   * 카드 전체 조희 controller
   *
   * @param columnId
   * @return ResponseEntity
   */
  @GetMapping("")
  public ResponseEntity<CommonResponse<List<CardResponseDto>>> findAllCards(
      @RequestParam(value = "column-id") Long columnId) {

    return ResponseEntity.ok(
        CommonResponse.<List<CardResponseDto>>builder()
            .response(ResponseEnum.FIND_CARD)
            .data(cardService.findAllCards(columnId))
            .build());

  }

  /**
   * 카드 단건 조회 controller
   *
   * @param cardId
   * @return ResponseEntity
   */
  @GetMapping("/{card-id}")
  public ResponseEntity<CommonResponse<CardResponseDto>> findOneCard(
      @PathVariable(value = "card-id") Long cardId) {

    return ResponseEntity.ok(
        CommonResponse.<CardResponseDto>builder()
            .response(ResponseEnum.FIND_CARD)
            .data(cardService.findOneCard(cardId))
            .build());
  }

  /**
   * 카드 삭제  controller
   *
   * @param cardId
   * @return ResponseEntity
   */
  @DeleteMapping("/{card-id}")
  public ResponseEntity<CommonResponse<CardResponseDto>> deleteCard(
      @PathVariable(value = "card-id") Long cardId) {

    cardService.deleteCard(cardId);

    return ResponseEntity.ok(
        CommonResponse.<CardResponseDto>builder()
            .response(ResponseEnum.DELETE_CARD)
            .data(null)
            .build());
  }
}

