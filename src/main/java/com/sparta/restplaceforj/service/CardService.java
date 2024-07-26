package com.sparta.restplaceforj.service;

import com.sparta.restplaceforj.dto.CardRequestDto;
import com.sparta.restplaceforj.dto.CardResponseDto;
import com.sparta.restplaceforj.dto.CardUpdateRequestDto;
import com.sparta.restplaceforj.dto.ColumnResponseDto;
import com.sparta.restplaceforj.entity.Card;
import com.sparta.restplaceforj.entity.Column;
import com.sparta.restplaceforj.entity.Plan;
import com.sparta.restplaceforj.entity.Post;
import com.sparta.restplaceforj.repository.CardRepository;
import com.sparta.restplaceforj.repository.ColumnRepository;
import com.sparta.restplaceforj.repository.PlanRepository;
import com.sparta.restplaceforj.repository.PostRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import org.springframework.web.filter.RequestContextFilter;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class CardService {

  private final ColumnRepository columnRepository;
  private final CardRepository cardRepository;
  private final RequestContextFilter requestContextFilter;

  /**
   * 카드 생성 로직
   *
   * @param
   * @param columId
   * @param title
   * @param address
   * @param placeName
   * @param memo
   * @return CardResponseDto
   */
  public CardResponseDto createCard(Long columId, String title, String address, String placeName,
      String memo) {
    Column column = columnRepository.findColumnById(columId);
    Card card = new Card(title, address, column, placeName, memo);
    cardRepository.save(card);
    return CardResponseDto.builder().id(card.getId()).build();
  }

  /**
   * 카드 수정 로직
   *
   * @param cardId
   * @param reqDto
   * @return CardResponseDto
   */

  public CardResponseDto updateCard(Long cardId, CardUpdateRequestDto reqDto) {
    
    Card card = cardRepository.findCardById(cardId);

    String title = card.getTitle();
    if (reqDto.getTitle() != null) {
      title = reqDto.getTitle();
    }

    String address = card.getAddress();
    if (reqDto.getAddress() != null) {
      address = reqDto.getAddress();
    }

    String memo = card.getMemo();
    if (reqDto.getMemo() != null) {
      memo = reqDto.getMemo();
    }

    String placeName = card.getPlaceName();
    if (reqDto.getPlaceName() != null) {
      placeName = reqDto.getPlaceName();
    }
    card.builder()
        .title(title)
        .address(address)
        .memo(memo)
        .placeName(placeName)
        .build();

    cardRepository.save(card);

    return CardResponseDto.builder().id(card.getId()).build();
  }

  /**
   * 카드 전체 조회
   *
   * @param columId
   * @return List<CardResponseDto>
   */
  public List<CardResponseDto> findAllCards(Long columId) {
    Column column = columnRepository.findColumnById(columId);
//    List<Card> cards = cardRepository.findAllByColumn(column);
//
//    List<CardResponseDto> items = new ArrayList<>(cards.size());
//    for (Card card : cards) {
//      CardResponseDto item = CardResponseDto.builder()
//          .title(card.getTitle())
//          .address(card.getAddress())
//          .placeName(card.getPlaceName())
//          .memo(card.getMemo())
//          .build();
//
//      items.add(item);
//    }
//
//    return items;

    return cardRepository.findAllByColumn(column);
  }

  /**
   * 카드 단건 조희
   *
   * @param cardId
   * @return CardResponseDto
   */
  public CardResponseDto findOneCard(Long cardId) {
    Card card = cardRepository.findCardById(cardId);
    return CardResponseDto.builder()
        .id(cardId)
        .title(card.getTitle())
        .address(card.getAddress())
        .placeName(card.getPlaceName())
        .memo(card.getMemo())
        .build();
  }

  /**
   * 카드 삭제 로직
   *
   * @param cardId
   */
  public void deleteCard(Long cardId) {
    Card card = cardRepository.findCardById(cardId);
    cardRepository.delete(card);
  }
}
