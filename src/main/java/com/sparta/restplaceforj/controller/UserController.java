package com.sparta.restplaceforj.controller;

import com.sparta.restplaceforj.common.CommonResponse;
import com.sparta.restplaceforj.common.ResponseEnum;
import com.sparta.restplaceforj.dto.UserResignRequestDto;
import com.sparta.restplaceforj.dto.UserResignResponseDto;
import com.sparta.restplaceforj.dto.UserSignUpRequestDto;
import com.sparta.restplaceforj.dto.UserSignUpResponseDto;
import com.sparta.restplaceforj.security.UserDetailsImpl;
import com.sparta.restplaceforj.dto.*;
import com.sparta.restplaceforj.security.UserDetailsImpl;
import com.sparta.restplaceforj.service.UserService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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
     * 유저 삭제 controller
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

    /**
     * 유저 프로필 조회 controller
     *
     * @param userId
     * @return CommonResponse
     */
    @GetMapping("/{user-id}")
    public ResponseEntity<CommonResponse<UserProfileResponseDto>> getUserProfile(@PathVariable("user-id") Long userId) {
        UserProfileResponseDto userProfileResponseDto = userService.getUserProfile(userId);
        return ResponseEntity.ok(
                CommonResponse.<UserProfileResponseDto>builder()
                        .response(ResponseEnum.GET_USER_PROFILE)
                        .data(userProfileResponseDto)
                        .build()
        );
    }


    /**
     * 유저 프로필 사진 업로드 controller
     *
     * @param multipartFile
     * @param userDetails
     * @return CommonResponse
     */
    @PostMapping("/profileImage")
    public ResponseEntity<CommonResponse<UpdateUserProfileImageResponseDto>> createUserProfileImage(
            @RequestParam("images") MultipartFile multipartFile,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        UpdateUserProfileImageResponseDto updateUserProfileImageResponseDto = userService.updateUserProfileImage(multipartFile, userDetails.getUser());
        return ResponseEntity.ok(
                CommonResponse.<UpdateUserProfileImageResponseDto>builder()
                        .response(ResponseEnum.CREATE_USER_PROFILE_IMAGE)
                        .data(updateUserProfileImageResponseDto)
                        .build()
        );
    }



    /**
     * 유저 프로필 수정 controller
     *
     * @param userUpdateRequestDto
     * @param userDetails
     * @return CommonResponse
     */
    @PatchMapping("/{user-id}")
    public ResponseEntity<CommonResponse<UserProfileResponseDto>> updateUserProfile(
            @RequestBody @Valid UserUpdateRequestDto userUpdateRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable("user-id") Long userId) {
        UserProfileResponseDto userProfileResponseDto = userService.updateUserProfile(userUpdateRequestDto, userDetails.getUser(), userId);
        return ResponseEntity.ok(
                CommonResponse.<UserProfileResponseDto>builder()
                        .response(ResponseEnum.UPDATE_USER_PROFILE)
                        .data(userProfileResponseDto)
                        .build()
        );
    }

}
