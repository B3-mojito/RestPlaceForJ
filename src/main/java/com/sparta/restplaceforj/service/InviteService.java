package com.sparta.restplaceforj.service;


import com.sparta.restplaceforj.dto.AuthCheckResponseDto;
import com.sparta.restplaceforj.entity.Coworker;
import com.sparta.restplaceforj.entity.Plan;
import com.sparta.restplaceforj.entity.User;
import com.sparta.restplaceforj.exception.CommonException;
import com.sparta.restplaceforj.exception.ErrorEnum;
import com.sparta.restplaceforj.repository.CoworkerRepository;
import com.sparta.restplaceforj.repository.PlanRepository;
import com.sparta.restplaceforj.repository.UserRepository;
import com.sparta.restplaceforj.provider.AuthCodeProvider;
import com.sparta.restplaceforj.provider.MailProvider;
import com.sparta.restplaceforj.provider.RedisProvider;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InviteService {

    private final UserRepository userRepository;
    private final PlanRepository planRepository;
    private final CoworkerRepository coworkerRepository;
    private final RedisProvider redisProvider;
    private final AuthCodeProvider authCodeProvider;
    private final MailProvider mailProvider;

    // 1.1 이메일 송신
    public void sendEmail(String toEmail, Long planId, User user) throws MessagingException {
        // 유효성 검사
        checkEmailInvalidation(toEmail, user, planId);

        // 인증 코드 생성
        String authCode = authCodeProvider.createAuthCode();

        // 송신
        mailProvider.sendMail(toEmail, planId, authCode);

        // redis에 송신할 이메일 주소, 인증 코드, 만료 기한(30분) 설정
        redisProvider.setValuesWithTimeout(authCode, toEmail, AuthCodeProvider.EXPIRATION_TIME);
    }

    // 1.2 유효성 검사
    public void checkEmailInvalidation(String email, User user, Long planId) {
        //로그인한 유저 스스로 초대할 수 없도록 예외 처리
        if(email.equals(user.getEmail())) {
            throw new CommonException(ErrorEnum.SELF_INVITATION_ERROR);
        }

        // 해당 이메일로 가입한 유저(초대받을 유저)가 있는지 확인
        User willInvitedUser = userRepository.findByEmailOrThrow(email);

        // planId에 해당하는 plan이 존재하는지 확인
        Plan plan = planRepository.findByIdOrThrow(planId);

        // 초대하려는 유저가 이미 공동작업자로 초대되어 있는지 확인
        if (coworkerRepository.existsByUserIdAndPlanId(willInvitedUser.getId(), plan.getId())) {
            throw new CommonException(ErrorEnum.DUPLICATE_INVITATION);
        }
    }

    // 2.1 초대한 유저를 공동작업자로 추가
    @Transactional
    public AuthCheckResponseDto createCoworker(Long planId, String authCode) {
        // authCode 유효성 검사, 유저의 이메일 가져오기
        String email = authCodeProvider.checkAuthCodeInvalidation(authCode);

        User invitedUser = userRepository.findByEmailOrThrow(email);
        Plan plan = planRepository.findByIdOrThrow(planId);

        // 초대하려는 유저를 공동 작업자로 추가
        Coworker coworker = Coworker.builder()
                .user(invitedUser)
                .plan(plan)
                .build();

        coworkerRepository.save(coworker);

        return AuthCheckResponseDto.builder()
                .coworkerId(coworker.getId())
                .build();
    }


}
