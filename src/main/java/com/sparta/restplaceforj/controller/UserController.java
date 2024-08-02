package com.sparta.restplaceforj.controller;

import com.sparta.restplaceforj.common.CommonResponse;
import com.sparta.restplaceforj.common.ResponseEnum;
import com.sparta.restplaceforj.dto.UserResignRequestDto;
import com.sparta.restplaceforj.dto.UserResignResponseDto;
import com.sparta.restplaceforj.dto.UserSignUpRequestDto;
import com.sparta.restplaceforj.dto.UserSignUpResponseDto;
import com.sparta.restplaceforj.security.UserDetailsImpl;
import com.sparta.restplaceforj.dto.*;
import com.sparta.restplaceforj.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    /**
     * 유저 생성 controller
     *
     * @param userSignUpRequestDto 필드명: email, password, name, nickname
     * @return userSignUpResponseDto 필드명: email, name, nickname;
     */
    @PostMapping
    public ResponseEntity<CommonResponse<UserSignUpResponseDto>> createUser(
            @RequestBody @Valid UserSignUpRequestDto userSignUpRequestDto) {
        UserSignUpResponseDto userSignUpResponseDto = userService.createUser(userSignUpRequestDto);
        return ResponseEntity.ok(
                CommonResponse.<UserSignUpResponseDto>builder()
                        .response(ResponseEnum.CREATE_USER)
                        .data(userSignUpResponseDto)
                        .build()
        );
    }


    /**
     * 유저 삭제 controller
     *
     * @param userResignRequestDto 팔드명: password
     * @return userResignResponseDto 필드명: email, role
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
     * @return UserProfileResponseDto 필드명:nickname, bio, profilePicture
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
     * @return UpdateUserProfileImageResponseDto 필드명: profileImageUrl
     */
    @PostMapping("/{user-id}/profileImage")
    public ResponseEntity<CommonResponse<UpdateUserProfileImageResponseDto>> createUserProfileImage(
            @RequestParam("images") MultipartFile multipartFile,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable("user-id") Long userId) throws IOException {
        UpdateUserProfileImageResponseDto updateUserProfileImageResponseDto = userService.updateUserProfileImage(multipartFile, userDetails.getUser(), userId);
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
     * @param userUpdateRequestDto 필드명: nickname, bio, currentPassword, newPassword, confirmPassword;
     * @param userDetails
     * @return UserProfileResponseDto 필드명:nickname, bio, profilePicture
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
