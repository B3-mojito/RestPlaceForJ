package com.sparta.restplaceforj.controller;


import com.sparta.restplaceforj.common.CommonResponse;
import com.sparta.restplaceforj.common.ResponseEnum;
import com.sparta.restplaceforj.dto.AuthCheckRequestDto;
import com.sparta.restplaceforj.dto.AuthCheckResponseDto;
import com.sparta.restplaceforj.dto.EmailRequestDto;
import com.sparta.restplaceforj.security.UserDetailsImpl;
import com.sparta.restplaceforj.service.InviteService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;



@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/plans")
public class InviteController {

    private final InviteService inviteService;

    @PostMapping("/invite")
    public ResponseEntity<CommonResponse> checkEmailAndSendAuthCode(
            @RequestBody @Valid EmailRequestDto emailDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws MessagingException {

        inviteService.checkEmailInvalidation(emailDto.getEmail(), userDetails.getUser());
        inviteService.sendEmail(emailDto.getEmail());

        return ResponseEntity.ok(
                CommonResponse.builder()
                        .response(ResponseEnum.SEND_AUTH_CODE)
                        .data(null)
                        .build()
        );
    }

    @PostMapping("/{plan-id}/authCode")
    public ResponseEntity<CommonResponse<AuthCheckResponseDto>> AuthCheckAndCreateCoworker(
            @RequestBody AuthCheckRequestDto authCheckRequestDto,
            @PathVariable("plan-id") Long planId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        String email = inviteService.verifyAuthCode(authCheckRequestDto.getAuthCode());
        inviteService.addCoworkerItself(userDetails.getUser(), planId);
        AuthCheckResponseDto authCheckResponseDto = inviteService.createCoworker(planId, email);


        return ResponseEntity.ok(
                CommonResponse.<AuthCheckResponseDto>builder()
                        .response(ResponseEnum.CREATE_COWORKER)
                        .data(authCheckResponseDto)
                        .build()
        );
    }






}
