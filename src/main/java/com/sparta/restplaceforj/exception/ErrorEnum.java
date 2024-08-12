package com.sparta.restplaceforj.exception;

import com.sparta.restplaceforj.common.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorEnum implements Response {
  // 공통
  BAD_REQUEST("잘못된 요청입니다.", HttpStatus.BAD_REQUEST),

  //user error
  USER_NOT_FOUND("유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  DUPLICATED_EMAIL("해당 이메일로 가입한 유저가 존재합니다.", HttpStatus.CONFLICT),
  DUPLICATED_NICKNAME("해당 닉네임을 가진 유저가 존재합니다.", HttpStatus.CONFLICT),
  INVALID_ACCESS("권한이 없습니다.", HttpStatus.UNAUTHORIZED),
  FORBIDDEN_ACCESS("접근할 수 없습니다.", HttpStatus.FORBIDDEN),
  BAD_PASSWORD("비밀번호가 일치하지 않습니다.", HttpStatus.NOT_FOUND),
  ALREADY_DEACTIVATE("이미 탈퇴한 회원입니다.", HttpStatus.BAD_REQUEST),

  //auth error
  INVALID_JWT("유효하지 않는 JWT 입니다.", HttpStatus.UNAUTHORIZED),
  EXPIRED_JWT("만료된 JWT 입니다.", HttpStatus.FORBIDDEN),
  NOT_FOUND_TOKEN("토큰을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  NOT_FOUND_AUTHENTICATION_INFO("인증 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

  //Post error
  POST_NOT_FOUND("글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  THEME_NOT_FOUND("테마를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  SORT_NOT_FOUND("정렬 불가능 합니다.", HttpStatus.NOT_FOUND),
  ONLY_IMAGE("이미지 파일만 올릴 수 있습니다.", HttpStatus.FORBIDDEN),
  IMAGE_NOT_FOUND("이미지 파일을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  POST_MISMATCH("작성자만 삭제 가능합니다.", HttpStatus.FORBIDDEN),

  //comment
  COMMENT_NOT_FOUND("댓글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  COMMENT_MISMATCH("작성자만 수정 가능합니다.", HttpStatus.FORBIDDEN),

  //like
  POST_LIKE_NOT_FOUND("글 좋아요를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  COMMENT_LIKE_NOT_FOUND("댓글 좋아요를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),


  //Plan error
  PLAN_NOT_FOUND("플랜을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

  //Invitation error
  INVALID_AUTH_CODE("잘못된 인증 코드입니다.", HttpStatus.UNAUTHORIZED),
  DUPLICATE_INVITATION("이미 공동 작업자로 참여하고 있는 유저입니다.", HttpStatus.CONFLICT),
  SELF_INVITATION_ERROR("본인은 초대할 수 없습니다.", HttpStatus.BAD_REQUEST),

  // Column errors
  COLUMN_NOT_FOUND("컬럼을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

  //Column move
  DEFAULT_COLUMN_NOT_FOUND("디폴트 카드가 없습니다.", HttpStatus.NOT_FOUND),
  MULTIPLE_DEFAULT_COLUMN("디폴트 카드가 중복되었습니다.", HttpStatus.BAD_REQUEST),

  // Card errors
  CARD_NOT_FOUND("카드를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),


  // Global errors
  VALID_ERROR("잘못된 접근 값입니다.", HttpStatus.BAD_REQUEST),
  GLOBAL_ERROR("처리하지 않은 예외", HttpStatus.INTERNAL_SERVER_ERROR);


  private final String message;
  private final HttpStatus httpStatus;

}
