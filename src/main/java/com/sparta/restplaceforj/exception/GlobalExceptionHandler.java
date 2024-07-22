package com.sparta.restplaceforj.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sparta.restplaceforj.common.CommonResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler({CommonException.class})
	public ResponseEntity<CommonResponse> illegalArgumentExceptionHandler(CommonException ex) {
		return ResponseEntity.ok(
			CommonResponse.builder()
				.response(ex.getResponse()).build());
	}
}

