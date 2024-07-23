package com.sparta.restplaceforj.repository;

import com.sparta.restplaceforj.entity.Card;
import com.sparta.restplaceforj.exception.CommonException;
import com.sparta.restplaceforj.exception.ErrorEnum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
    default Card findCardById(Long id) {
        return findById(id).orElseThrow(()->new CommonException(ErrorEnum.CARD_NOT_FOUND));
    }
}
