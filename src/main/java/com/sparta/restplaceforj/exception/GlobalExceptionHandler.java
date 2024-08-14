package com.sparta.restplaceforj.exception;

import com.sparta.restplaceforj.common.CommonResponse;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<CommonResponse<String>> ExceptionHandler(Exception ex) {
    log.warn("handleAllException", ex);
    return ResponseEntity.ok(
        CommonResponse.<String>builder()
            .response(ErrorEnum.GLOBAL_ERROR)
            .data(ex.getMessage())
            .build()
    );

  }

  @ExceptionHandler(ExpiredJwtException.class)
  public ResponseEntity<CommonResponse<String>> ExceptionHandler(ExpiredJwtException ex) {
    log.warn(ErrorEnum.EXPIRED_REFRESH_TOKEN.getMessage());
    return ResponseEntity.status(403).body(
        CommonResponse.<String>builder()
            .response(ErrorEnum.EXPIRED_REFRESH_TOKEN)
            .data(ex.getMessage())
            .build()
    );
  }

  @ExceptionHandler({CommonException.class})
  public ResponseEntity<CommonResponse> illegalArgumentExceptionHandler(CommonException ex) {
    return ResponseEntity.status(404).body(
        CommonResponse.builder()
            .response(ex.getResponse())
            .build());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<CommonResponse<StringBuilder>> processValidationError(
      MethodArgumentNotValidException ex) {
    BindingResult bindingResult = ex.getBindingResult();

    StringBuilder builder = new StringBuilder();
    for (FieldError fieldError : bindingResult.getFieldErrors()) {
      builder.append("[");
      builder.append(fieldError.getField());
      builder.append("](은)는 ");
      builder.append(fieldError.getDefaultMessage());
      builder.append(" 입력된 값: [");
      builder.append(fieldError.getRejectedValue());
      builder.append("]");
    }

    log.warn("handleAllException", ex);
    return ResponseEntity.ok(
        CommonResponse.<StringBuilder>builder()
            .response(ErrorEnum.BAD_REQUEST)
            .data(builder)
            .build()
    );
  }
}

