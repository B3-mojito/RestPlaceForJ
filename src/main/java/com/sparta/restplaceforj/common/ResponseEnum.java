package com.sparta.restplaceforj.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseEnum {

    //card
    CREATE_CARD("카드 생성 완료", HttpStatus.CREATED),



    //user



    //post


    //comment


    //like


    //column

    ;

    private final String message;
    private final HttpStatus httpStatus;


}
