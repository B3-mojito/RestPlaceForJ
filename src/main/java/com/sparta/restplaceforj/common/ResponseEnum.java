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
  ADD_POST("연관 게시물 추가완료", HttpStatus.CREATED),
  MOVE_CARD("컬럼간 카드 이동완료", HttpStatus.OK),

  //user
  LOGIN_SUCCESS("로그인 성공", HttpStatus.OK),
  KAKAO_LOGIN_SUCCESS("로그인 성공", HttpStatus.OK),
  LOGOUT_SUCCESS("로그아웃 성공", HttpStatus.OK),
  CREATE_USER("유저 생성 완료", HttpStatus.CREATED),
  DELETE_USER("유저 탈퇴 완료", HttpStatus.OK),
  GET_USER_PROFILE("유저 프로필 조회 완료", HttpStatus.OK),
  UPDATE_USER_PROFILE("유저 프로필 수정 완료", HttpStatus.OK),
  CREATE_USER_PROFILE_IMAGE("유저 프로필 업로드 완료", HttpStatus.CREATED),
  UPDATE_TOKEN("토큰 재발급 완료", HttpStatus.OK),
  FORBIDDEN_ACCESS("접근할 수 없습니다.", HttpStatus.FORBIDDEN),
  INVALID_ACCESS("권한이 없습니다.", HttpStatus.UNAUTHORIZED),
  LOGIN_FAIL("로그인 실패", HttpStatus.UNAUTHORIZED),


  //post
  CREATE_POST("글 생성 완료", HttpStatus.CREATED),
  DELETE_POST("글 삭제 완료", HttpStatus.OK),
  GET_POST("글 단권 조회 완료", HttpStatus.OK),
  GET_POST_LIST("글 전체 조회 완료", HttpStatus.OK),
  GET_POST_ID_TITLE_LIST("글 아이디 제목 조회 완료", HttpStatus.OK),
  UPDATE_POST("글 수정 완료", HttpStatus.OK),
  GET_MY_POST_LIST("본인 작성 게시물 조회 완료", HttpStatus.OK),
  GET_USER_POST_LIST("유저가 작성한 게시물 조회 완료", HttpStatus.OK),

  //image
  CREATE_IMAGE("사진 저장 완료", HttpStatus.CREATED),
  GET_IMAGE("사진 조회 완료", HttpStatus.OK),
  DELETE_IMAGE("사진 삭제 완료", HttpStatus.OK),

  //comment
  CREATE_COMMENT("댓글 생성 완료", HttpStatus.CREATED),
  GET_COMMENT_LIST("댓글 조회 완료", HttpStatus.OK),
  UPDATE_COMMENT("댓글 수정 완료", HttpStatus.OK),
  DELETE_COMMENT("댓글 삭제 완료", HttpStatus.OK),
  //comment

  //like
  CREATE_POST_LIKE_COMMENT("글 좋아요 생성 완료", HttpStatus.CREATED),
  DELETE_POST_LIKE_COMMENT("글 좋아요 삭제 완료", HttpStatus.OK),
  CREATE_COMMENT_LIKE_COMMENT("댓글 좋아요 생성 완료", HttpStatus.CREATED),
  DELETE_COMMENT_LIKE_COMMENT("댓글 좋아요 삭제 완료", HttpStatus.OK),


  //plan
  CREATE_PLAN("플랜 생성 완료", HttpStatus.CREATED),
  UPDATE_PLAN("플랜 수정 완료", HttpStatus.OK),
  DELETE_PLAN("플랜 삭제 완료", HttpStatus.OK),
  GET_PLAN_LIST("플랜 전체 조회 완료", HttpStatus.OK),
  GET_PLAN("플랜 조회 완료", HttpStatus.OK),

  //invitation
  SEND_AUTH_CODE("인증 코드 전송 완료", HttpStatus.OK),
  AUTH_CODE_CHECK("인증 완료", HttpStatus.OK),
  CREATE_COWORKER("공동 작업자 추가 완료", HttpStatus.CREATED),

  //column
  CREATE_COLUMN("컬럼 생성 완료", HttpStatus.CREATED),
  UPDATE_COLUMN("컬럼 수정 완료", HttpStatus.OK),
  DELETE_COLUMN("컬럼 삭제 완료", HttpStatus.OK),
  GET_COLUMN("컬럼 조회 성공", HttpStatus.OK);

  private final String message;
  private final HttpStatus httpStatus;


}
