package com.sparta.restplaceforj.exception;

import com.sparta.restplaceforj.common.Response;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommonException extends RuntimeException {

    private final Response response;

}
