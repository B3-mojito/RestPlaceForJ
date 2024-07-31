package com.sparta.restplaceforj.exception;

import com.sparta.restplaceforj.common.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<CommonResponse> ExceptionHandler(Exception ex) {
    log.warn("handleAllException", ex);
    return ResponseEntity.ok(
        CommonResponse.builder()
            .response(ErrorEnum.GLOBAL_ERROR)
            .data(ex.getMessage())
            .build()
    );
  }

  @ExceptionHandler({CommonException.class})
  public ResponseEntity<CommonResponse> illegalArgumentExceptionHandler(CommonException ex) {
    return ResponseEntity.ok(
        CommonResponse.builder()
            .response(ex.getResponse()).build());
  }
}

