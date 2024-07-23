package com.sparta.restplaceforj.service;

import com.sparta.restplaceforj.dto.CardResponseDto;
import com.sparta.restplaceforj.dto.ColumnResponseDto;
import com.sparta.restplaceforj.entity.Card;
import com.sparta.restplaceforj.entity.Column;
import com.sparta.restplaceforj.entity.Plan;
import com.sparta.restplaceforj.entity.Post;
import com.sparta.restplaceforj.repository.CardRepository;
import com.sparta.restplaceforj.repository.ColumnRepository;
import com.sparta.restplaceforj.repository.PlanRepository;
import com.sparta.restplaceforj.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CardService {
    private final ColumnRepository columnRepository;
    private final PostRepository postRepository;
    private final CardRepository cardRepository;

    /**
     * 카드 생성 로직
     *
     * @param postId
     * @param columId
     * @param title
     * @param address
     * @param time
     * @return CardResponseDto
     */
    @Transactional
    public CardResponseDto createCard(Long postId, Long columId, String title, String address, LocalDateTime time) {
        Post post = postRepository.findPostById(postId);
        Column column = columnRepository.findColumnById(columId);
        Card card = new Card(title, address, time, column, post);
        cardRepository.save(card);
        return CardResponseDto.builder().title(card.getTitle()).build();
    }

    /**
     * 카드 생성 로직
     *
     * @param cardId
     * @param title
     * @param address
     * @param time
     * @return CardResponseDto
     */

    @Transactional
    public CardResponseDto updateCard(Long cardId, String title, String address, LocalDateTime time) {
        Card card = cardRepository.findCardById(cardId);
        card.update(title, address, time);
        return CardResponseDto.builder().title(card.getTitle()).build();
    }

//    @Transactional
//    public void deleteCard(Long cardId) {
//        Card card = cardRepository.findCardById(cardId);
//        cardRepository.delete(card);
//    }
}
