package com.sparta.restplaceforj.controller;

import com.sparta.restplaceforj.common.CommonResponse;
import com.sparta.restplaceforj.common.ResponseEnum;
import com.sparta.restplaceforj.dto.UserResignRequestDto;
import com.sparta.restplaceforj.dto.UserResignResponseDto;
import com.sparta.restplaceforj.dto.UserSignUpRequestDto;
import com.sparta.restplaceforj.dto.UserSignUpResponseDto;
import com.sparta.restplaceforj.security.UserDetailsImpl;
import com.sparta.restplaceforj.service.UserService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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


    /**
     * 유저 생성 controller
     *
     * @param userResignRequestDto
     * @return CommonResponse
     */
  @DeleteMapping
  public ResponseEntity<CommonResponse<UserResignResponseDto>> deleteUser(
          @AuthenticationPrincipal UserDetailsImpl userDetails,
          @RequestBody UserResignRequestDto userResignRequestDto) {
      UserResignResponseDto userResignResponseDto = userService.deleteUser(userDetails.getUser(), userResignRequestDto.getPassword());

    return ResponseEntity.ok(
            CommonResponse.<UserResignResponseDto>builder()
                    .response(ResponseEnum.DELETE_USER)
                    .data(userResignResponseDto)
                    .build()
    );

  }

}
