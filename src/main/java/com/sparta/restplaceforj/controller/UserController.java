package com.sparta.restplaceforj.controller;

import com.sparta.restplaceforj.common.CommonResponse;
import com.sparta.restplaceforj.common.ResponseEnum;
import com.sparta.restplaceforj.dto.UserSignUpRequestDto;
import com.sparta.restplaceforj.dto.UserSignUpResponseDto;
import com.sparta.restplaceforj.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    /**
     * 유저 생성 controller
     *
     * @param userSignUprequestDto
     * @return CommonResponse
     */
    @PostMapping
    public ResponseEntity<CommonResponse<UserSignUpResponseDto>> createUser(
            @RequestBody @Valid UserSignUpRequestDto userSignUprequestDto) {
        UserSignUpResponseDto userSignUpresponseDto = userService.createUser(userSignUprequestDto);
        return ResponseEntity.ok(
                CommonResponse.<UserSignUpResponseDto>builder()
                        .response(ResponseEnum.CREATE_USER)
                        .data(userSignUpresponseDto)
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<CommonResponse<UserSignUpResponseDto>> getUserProfile(
            @RequestBody @Valid UserSignUpRequestDto userSignUprequestDto) {
        UserSignUpResponseDto userSignUpresponseDto = userService.createUser(userSignUprequestDto);
        return ResponseEntity.ok(
                CommonResponse.<UserSignUpResponseDto>builder()
                        .response(ResponseEnum.CREATE_USER)
                        .data(userSignUpresponseDto)
                        .build()
        );
    }


}
