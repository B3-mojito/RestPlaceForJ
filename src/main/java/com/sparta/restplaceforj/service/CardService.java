package com.sparta.restplaceforj.service;

import com.sparta.restplaceforj.dto.CardDetailResponseDto;
import com.sparta.restplaceforj.dto.CardRequestDto;
import com.sparta.restplaceforj.dto.CardResponseDto;
import com.sparta.restplaceforj.dto.CardUpdateRequestDto;
import com.sparta.restplaceforj.dto.PostResponseDto;
import com.sparta.restplaceforj.entity.Card;
import com.sparta.restplaceforj.entity.Column;
import com.sparta.restplaceforj.repository.CardRepository;
import com.sparta.restplaceforj.repository.ColumnRepository;
import com.sparta.restplaceforj.repository.PostRepository;
import com.sparta.restplaceforj.repository.RelatedPostRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class CardService {

  private final ColumnRepository columnRepository;
  private final CardRepository cardRepository;
  private final PostRepository postRepository;
  private final RelatedPostRepository relatedPostRepository;

  /**
   * 카드 생성 로직
   *
   * @param
   * @param columId
   * @param cardRequestDto
   * @return CardResponseDto
   */
  @Transactional
  public CardResponseDto createCard(Long columId, CardRequestDto cardRequestDto) {
    Column column = columnRepository.findByIdOrThrow(columId);

    Card card = Card.builder()
        .column(column)
        .title(cardRequestDto.getTitle())
        .address(cardRequestDto.getAddress())
        .placeName(cardRequestDto.getPlaceName())
        .startedAt(cardRequestDto.getStartedAt())
        .endedAt(cardRequestDto.getEndedAt())
        .memo(cardRequestDto.getMemo())
        .build();

    cardRepository.save(card);
    return CardResponseDto.builder()
        .id(card.getId())
        .title(card.getTitle())
        .address(card.getAddress())
        .placeName(card.getPlaceName())
        .startedAt(card.getStartedAt())
        .endedAt(card.getEndedAt())
        .memo(card.getMemo())
        .build();
  }

  /**
   * 카드 수정 로직
   *
   * @param cardId
   * @param cardUpdateRequestDto
   * @return CardResponseDto
   */
  @Transactional
  public CardResponseDto updateCard(Long cardId, CardUpdateRequestDto cardUpdateRequestDto) {

    Card card = cardRepository.findByIdOrThrow(cardId);
    String title = cardUpdateRequestDto.getTitle();
    String address = cardUpdateRequestDto.getAddress();
    String placeName = cardUpdateRequestDto.getPlaceName();
    LocalTime startedAt = cardUpdateRequestDto.getStartedAt();
    LocalTime endedAt = cardUpdateRequestDto.getEndedAt();
    String memo = cardUpdateRequestDto.getMemo();
    if (cardUpdateRequestDto.getTitle() == null) {
      title = card.getTitle();
    }
    if (cardUpdateRequestDto.getAddress() == null) {
      address = card.getAddress();
    }
    if (cardUpdateRequestDto.getPlaceName() == null) {
      placeName = card.getPlaceName();
    }
    if (cardUpdateRequestDto.getStartedAt() == null) {
      startedAt = card.getStartedAt();
    }
    if (cardUpdateRequestDto.getEndedAt() == null) {
      endedAt = card.getEndedAt();
    }
    if (cardUpdateRequestDto.getMemo() == null) {
      memo = card.getMemo();
    }
    card.updateCard(CardUpdateRequestDto.builder()
        .title(title)
        .address(address)
        .placeName(placeName)
        .startedAt(startedAt)
        .endedAt(endedAt)
        .memo(memo)
        .build());

    cardRepository.save(card);

    return CardResponseDto.builder()
        .id(card.getId())
        .title(card.getTitle())
        .address(card.getAddress())
        .placeName(card.getPlaceName())
        .startedAt(card.getStartedAt())
        .endedAt(card.getEndedAt())
        .memo(card.getMemo())
        .build();
  }

  /**
   * 카드 전체 조회
   *
   * @param columId
   * @return List<CardResponseDto>
   */
  public List<CardResponseDto> getCardList(Long columId) {
    Column column = columnRepository.findByIdOrThrow(columId);

    return cardRepository.findAllByColumn(column);
  }

  /**
   * 카드 단건 조희
   *
   * @param cardId
   * @return CardResponseDto
   */
  public CardDetailResponseDto getCard(Long cardId) {
    Card card = cardRepository.findByIdOrThrow(cardId);
    List<PostResponseDto> postResponseDtoList = relatedPostRepository.findPostsByCardId(cardId);
    return CardDetailResponseDto.builder()
        .id(cardId)
        .title(card.getTitle())
        .address(card.getAddress())
        .placeName(card.getPlaceName())
        .memo(card.getMemo())
        .postList(postResponseDtoList)
        .build();
  }

  @Transactional
  public void deleteCard(Long cardId) {
    Card card = cardRepository.findByIdOrThrow(cardId);
    cardRepository.delete(card);
  }

  /**
   * 카드 컬럼 이동
   *
   * @param cardId
   * @param columnId
   * @return CardResponseDto
   */
  @Transactional
  public void updateCardColumn(Long cardId, Long columnId) {
    Column column = columnRepository.findByIdOrThrow(columnId);
    Card card = cardRepository.findByIdOrThrow(cardId);
    card.changeColumn(column);
    cardRepository.save(card);
  }
}

