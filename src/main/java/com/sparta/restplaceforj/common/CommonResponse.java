package com.sparta.restplaceforj.common;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CommonResponse<T> {

    private final HttpStatus statusCode;
    private final String message;
    private final T data;

    @Builder
    public CommonResponse(Response response, T data) {
        this.statusCode = response.getHttpStatus();
        this.message = response.getMessage();
        this.data = data;
    }

}