package com.sparta.restplaceforj.exception;


import com.sparta.restplaceforj.common.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<CommonResponse> illegalArgumentExceptionHandler(ErrorMessageEnum ex) {
          return ResponseEntity.ok(
                CommonResponse.builder()
                        .message(ex.getMessage())
                        .data(null).build()
        );
    }
}

