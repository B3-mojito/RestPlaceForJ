package com.sparta.restplaceforj.common;

import org.springframework.http.HttpStatus;

public interface Response {

    HttpStatus getHttpStatus();

    String getMessage();
}
