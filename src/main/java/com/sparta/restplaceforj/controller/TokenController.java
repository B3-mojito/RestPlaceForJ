package com.sparta.restplaceforj.controller;

import com.sparta.restplaceforj.common.CommonResponse;
import com.sparta.restplaceforj.common.ResponseEnum;
import com.sparta.restplaceforj.entity.User;
import com.sparta.restplaceforj.security.UserDetailsImpl;
import com.sparta.restplaceforj.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/users")
public class TokenController {

  private final TokenService tokenService;

  /**
   * 토큰 재발급 controller
   *
   * @param request
   * @param response
   * @return CommonResponse
   */
  @PostMapping("/reissue")
  public ResponseEntity<CommonResponse> updateToken(
      HttpServletRequest request,
      HttpServletResponse response
  ) {
    // refresh token을 사용하여 유저 정보 검증 및 새로운 토큰 발급
    User user = tokenService.validateAndGetUserFromRefreshToken(request);
    tokenService.updateToken(response, user);
    return ResponseEntity.ok(
        CommonResponse.builder()
            .response(ResponseEnum.UPDATE_TOKEN)
            .data(null)
            .build()
    );
  }
}
