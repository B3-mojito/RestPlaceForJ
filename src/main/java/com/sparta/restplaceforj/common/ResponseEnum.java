package com.sparta.restplaceforj.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseEnum implements Response {

  //card
  CREATE_CARD("카드 생성 완료", HttpStatus.CREATED),

  //user
  CREATE_USER("유저 생성 완료", HttpStatus.CREATED),


  //post
  CREATE_POST("글 생성 완료", HttpStatus.CREATED),
  DELETE_POST("글 삭제 완료", HttpStatus.OK),
  GET_POST_LIST("글 전체 조회 완료", HttpStatus.OK),
  GET_POST_ID_TITLE_LIST("글 아이디 제목 조회 완료", HttpStatus.OK),

  //comment

  //like

  //column
  CREATE_COLUMN("컬럼 생성 완료", HttpStatus.CREATED),
  UPDATE_COLUMN("컬럼 수정 완료", HttpStatus.OK),
  DELETE_COLUMN("컬럼 삭제 완료", HttpStatus.OK);

  private final String message;
  private final HttpStatus httpStatus;


}
