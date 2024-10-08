package com.sparta.restplaceforj.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.restplaceforj.common.CommonResponse;
import com.sparta.restplaceforj.common.ResponseEnum;
import com.sparta.restplaceforj.dto.UpdateUserProfileImageResponseDto;
import com.sparta.restplaceforj.dto.UserInfoResponseDto;
import com.sparta.restplaceforj.dto.UserPasswordUpdateDto;
import com.sparta.restplaceforj.dto.UserProfileResponseDto;
import com.sparta.restplaceforj.dto.UserResignRequestDto;
import com.sparta.restplaceforj.dto.UserResignResponseDto;
import com.sparta.restplaceforj.dto.UserSignUpRequestDto;
import com.sparta.restplaceforj.dto.UserSignUpResponseDto;
import com.sparta.restplaceforj.dto.UserUpdateRequestDto;
import com.sparta.restplaceforj.security.UserDetailsImpl;
import com.sparta.restplaceforj.service.KakaoService;
import com.sparta.restplaceforj.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/users")
public class UserController {

  private final UserService userService;
  private final KakaoService kakaoService;

  /**
   * 회원 가입 controller
   *
   * @param userSignUpRequestDto : email, password, name, nickname
   * @return userSignUpResponseDto : email, name, nickname;
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
   * 회원 탈퇴 controller
   *
   * @param userResignRequestDto : password
   * @return userResignResponseDto : email, role
   */
  @DeleteMapping
  public ResponseEntity<CommonResponse<UserResignResponseDto>> deleteUser(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestBody UserResignRequestDto userResignRequestDto) {
    UserResignResponseDto userResignResponseDto = userService
        .deleteUser(userDetails.getUser(), userResignRequestDto.getPassword());

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
   * @return UserProfileResponseDto :nickname, bio, profilePicture
   */
  @GetMapping("/{user-id}")
  public ResponseEntity<CommonResponse<UserProfileResponseDto>> getUserProfile(
      @PathVariable("user-id") Long userId) {
    UserProfileResponseDto userProfileResponseDto = userService.getUserProfile(userId);
    return ResponseEntity.ok(
        CommonResponse.<UserProfileResponseDto>builder()
            .response(ResponseEnum.GET_USER_PROFILE)
            .data(userProfileResponseDto)
            .build()
    );
  }

  /**
   * 마이페이지 프로필 조회 controller
   *
   * @param userDetails 로그인 유저 객체
   * @return UserProfileResponseDto :nickname, bio, profilePicture
   */
  @GetMapping("/myPage")
  public ResponseEntity<CommonResponse<UserProfileResponseDto>> getMyProfile(
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    UserProfileResponseDto userProfileResponseDto = userService.getUserProfile(userDetails.getUser()
        .getId());
    return ResponseEntity.ok(
        CommonResponse.<UserProfileResponseDto>builder()
            .response(ResponseEnum.GET_USER_PROFILE)
            .data(userProfileResponseDto)
            .build()
    );
  }

  @GetMapping("/me")
  public ResponseEntity<CommonResponse<UserInfoResponseDto>> getMyInfo(
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok(
        CommonResponse.<UserInfoResponseDto>builder()
            .response(ResponseEnum.GET_USER_PROFILE)
            .data(UserInfoResponseDto.builder()
                .userId(userDetails.getUser().getId()).build())
            .build()
    );
  }

  /**
   * 유저 프로필 사진 업로드 controller
   *
   * @param multipartFile
   * @param userDetails
   * @return UpdateUserProfileImageResponseDto : profileImageUrl
   */
  @PostMapping("/myPage/profile-image")
  public ResponseEntity<CommonResponse<UpdateUserProfileImageResponseDto>> createUserProfileImage(
      @RequestPart("images") MultipartFile multipartFile,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) throws IOException {
    UpdateUserProfileImageResponseDto updateUserProfileImageResponseDto = userService
        .updateUserProfileImage(multipartFile, userDetails.getUser());
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
   * @param userUpdateRequestDto : nickname, bio, currentPassword, newPassword, confirmPassword;
   * @param userDetails
   * @return UserProfileResponseDto :nickname, bio, profilePicture
   */
  @PatchMapping("/{user-id}")
  public ResponseEntity<CommonResponse<UserProfileResponseDto>> updateUserProfile(
      @RequestBody @Valid UserUpdateRequestDto userUpdateRequestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @PathVariable("user-id") Long userId) {
    UserProfileResponseDto userProfileResponseDto = userService.updateUserProfile(
        userUpdateRequestDto, userDetails.getUser(), userId);
    return ResponseEntity.ok(
        CommonResponse.<UserProfileResponseDto>builder()
            .response(ResponseEnum.UPDATE_USER_PROFILE)
            .data(userProfileResponseDto)
            .build()
    );
  }

  /**
   * 유저 비밀번호 수정 controller
   *
   * @param userPasswordUpdateDto : nickname, bio, currentPassword, newPassword, confirmPassword;
   * @param userDetails
   * @return UserProfileResponseDto :nickname, bio, profilePicture
   */
  @PatchMapping("/{user-id}/password")
  public ResponseEntity<CommonResponse<UserProfileResponseDto>> updateUserProfile(
      @RequestBody @Valid UserPasswordUpdateDto userPasswordUpdateDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @PathVariable("user-id") Long userId) {
    UserProfileResponseDto userProfileResponseDto = userService.updateUserPassword(
        userPasswordUpdateDto, userDetails.getUser(), userId);
    return ResponseEntity.ok(
        CommonResponse.<UserProfileResponseDto>builder()
            .response(ResponseEnum.UPDATE_USER_PROFILE)
            .data(userProfileResponseDto)
            .build()
    );
  }


  /**
   * 카카오 로그인 controller
   *
   * @param code : 카카오 서버에서 넘겨주는 인증 코드
   * @return null
   */
  @GetMapping("/kakao/callback")
  public ResponseEntity<CommonResponse> kakaoLogin(@RequestParam String code,
      HttpServletResponse response) throws JsonProcessingException {
    kakaoService.kakaoLogin(code, response);

    return ResponseEntity.ok(
        CommonResponse.<UserProfileResponseDto>builder()
            .response(ResponseEnum.KAKAO_LOGIN_SUCCESS)
            .data(null)
            .build()
    );

  }
}
