package com.sparta.restplaceforj.controller;


import com.sparta.restplaceforj.common.CommonResponse;
import com.sparta.restplaceforj.common.ResponseEnum;
import com.sparta.restplaceforj.dto.EmailRequestDto;
import com.sparta.restplaceforj.security.UserDetailsImpl;
import com.sparta.restplaceforj.service.InviteService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;



@Controller
@RequiredArgsConstructor
@RequestMapping("/v1/plans/{plan-id}/invite")
public class InviteController {

    private final InviteService inviteService;


    /**
     * 유저 이메일로 검색, 인증번호 발송 controller
     *
     * @param emailRequestDto : email
     * @param planId
     * @param userDetails
     * @return null
     */
    @ResponseBody
    @PostMapping
    public ResponseEntity<CommonResponse> checkEmailAndSendAuthCode(
            @RequestBody @Valid EmailRequestDto emailRequestDto,
            @PathVariable("plan-id") Long planId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws MessagingException {

        inviteService.sendEmail(emailRequestDto.getEmail(), planId, userDetails.getUser());

        return ResponseEntity.ok(
                CommonResponse.builder()
                        .response(ResponseEnum.SEND_AUTH_CODE)
                        .data(null)
                        .build()
        );
    }

    /**
     * 인증번호 유효성 검사, 공동작업자로 추가 controller
     *
     * @param planId
     * @param authCode
     * @return coworkerId
     */
    @GetMapping
    public String AuthCheckAndCreateCoworker(
            @PathVariable("plan-id") Long planId,
            @RequestParam String authCode) {

        inviteService.createCoworker(planId, authCode);

        return "redirect:http://localhost:3000/home";
    }

}
