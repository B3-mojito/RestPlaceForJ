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
  FIND_CARD("카드 조회 완료", HttpStatus.OK),
  DELETE_CARD("카트 삭제 완료", HttpStatus.OK),
  //user
  CREATE_USER("유저 생성 완료", HttpStatus.CREATED),
  GET_USER_PROFILE("유저 프로필 조회 완료", HttpStatus.OK),
  UPDATE_USER_PROFILE("유저 프로필 수정 완료", HttpStatus.OK),


  //post
  CREATE_POST("글 생성 완료", HttpStatus.CREATED),
  DELETE_POST("글 삭제 완료", HttpStatus.OK),
  GET_POST("글 단권 조회 완료", HttpStatus.OK),
  GET_POST_LIST("글 전체 조회 완료", HttpStatus.OK),
  GET_POST_ID_TITLE_LIST("글 아이디 제목 조회 완료", HttpStatus.OK),
  UPDATE_POST("글 수정 완료", HttpStatus.OK),

  //comment
  CREATE_COMMENT("댓글 생성 완료", HttpStatus.CREATED),
  GET_COMMENT_LIST("댓글 조회 완료", HttpStatus.OK),
  UPDATE_COMMENT("댓글 수정 완료", HttpStatus.OK),
  DELETE_COMMENT("댓글 삭제 완료", HttpStatus.OK),

  //like

  //plan
  CREATE_PLAN("플랜 생성 완료", HttpStatus.CREATED),
  UPDATE_PLAN("플랜 수정 완료", HttpStatus.OK),
  DELETE_PLAN("플랜 삭제 완료", HttpStatus.OK),
  GET_PLAN_LIST("플랜 전체 조회 완료", HttpStatus.OK),
  GET_PLAN("플랜 조회 완료", HttpStatus.OK),
  //column
  CREATE_COLUMN("컬럼 생성 완료", HttpStatus.CREATED),
  UPDATE_COLUMN("컬럼 수정 완료", HttpStatus.OK),
  DELETE_COLUMN("컬럼 삭제 완료", HttpStatus.OK),
  GET_COLUMN("컬럼 조회 성공", HttpStatus.OK);

  private final String message;
  private final HttpStatus httpStatus;


}
