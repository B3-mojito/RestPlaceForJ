package com.sparta.restplaceforj.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseEnum implements Response {

    //card
    CREATE_CARD("카드 생성 완료", HttpStatus.CREATED),
    UPDATE_CARD("카드 수정 완료", HttpStatus.OK),


    //user


    //post
    CREATE_POST("글 생성 완료", HttpStatus.CREATED),


    //comment


    //like


    //column
    CREATE_COLUMN("컬럼 생성 완료", HttpStatus.CREATED)
    ;

    private final String message;
    private final HttpStatus httpStatus;


}
