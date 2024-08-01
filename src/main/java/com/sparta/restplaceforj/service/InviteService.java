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
import com.sparta.restplaceforj.util.AuthCodeUtil;
import com.sparta.restplaceforj.util.RedisUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class InviteService {

    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    private final PlanRepository planRepository;
    private final CoworkerRepository coworkerRepository;
    private final RedisUtil redisUtil;
    private final AuthCodeUtil authCodeUtil;

    @Value("${spring.mail.username}")
    private String configEmail;

    public void checkEmailInvalidation(String email, User user) {
        //로그인한 유저 스스로 초대할 수 없도록 예외 처리
        if(email.equals(user.getEmail())) {
            throw new CommonException(ErrorEnum.BAD_REQUEST);
        }

        //해당 이메일로 가입한 유저가 있는지 확인
        if(!userRepository.existsByEmail(email)) {
            throw new CommonException(ErrorEnum.USER_NOT_FOUND);
        }
    }

    private MimeMessage createEmailForm(String email) throws MessagingException {

        // 인증 코드 생성
        String authCode = authCodeUtil.createAuthCode(email);

        // 메세지 설정
        MimeMessage message = mailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email); // 보내는 대상
        message.setSubject("[J의 안식처] 인증번호"); // 메일 제목
        message.setFrom(configEmail); //보내는 사람 메일 주소

        String msg="";
        msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">이메일 주소 확인</h1>";
        msg += "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">아래 확인 코드를 인증 화면에서 입력해주세요.</p>";
        msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
        msg += authCode;
        msg += "</td></tr></tbody></table></div>";
        message.setText(msg, "utf-8", "html");

        // redis에 송신할 이메일 주소, 인증 코드, 만료 기한(30분) 설정
        redisUtil.setValuesWithTimeout(authCodeUtil.SECRET_KEY+email, authCode, 60 * 30L);

        return message;
    }

    // 이메일 송신
    public void sendEmail(String toEmail) throws MessagingException {
        // 이미 인증 메일을 보냈다면 인증 코드가 중복될 수 있으므로 제거
        if (redisUtil.existData(toEmail)) {
            redisUtil.deleteValue(toEmail);
        }

        // mail.html 템플릿에 따라 이메일 포맷 생성
        MimeMessage emailForm = createEmailForm(toEmail);

        //송신
        mailSender.send(emailForm);
    }

    // 인증코드 검증
    public String verifyAuthCode(String authCode) {
        // 인증코드 기한이 유효한지 확인
        if (authCodeUtil.isTokenExpired(authCode)) {
            throw new CommonException(ErrorEnum.INVALID_AUTH_CODE);
        }

        // 유저가 보낸 코드에서 파싱한 이메일과 레디스에 저장된 이메일이 동일한지 확인
        String email = AuthCodeUtil.getEmailFromAuthCode(authCode);
        String authCodeFromRedis = redisUtil.getValues(authCodeUtil.SECRET_KEY+email)
                .substring(authCodeUtil.SECRET_KEY.length());
        if (!authCode.equals(authCodeFromRedis)) {
            throw new CommonException(ErrorEnum.INVALID_AUTH_CODE);
        }

        return email;
    }


    // 초대하려는 유저를 공동작업자로 추가
    public AuthCheckResponseDto createCoworker(Long planId, String email) {
        // 초대하려는 유저가 가입되어 있는지 확인
        User invitedUser = userRepository.findByEmailOrThrow(email);
        Plan plan = planRepository.findByIdOrThrow(planId);

        Coworker coworker = addCoworker(invitedUser, plan);

        return AuthCheckResponseDto.builder()
                .coworkerId(coworker.getId())
                .build();
    }

    // 초대자(로그인한 유저)를 공동작업자로 추가
    public void addCoworkerItself(User user, Long planId) {
        Plan plan = planRepository.findByIdOrThrow(planId);
        addCoworker(user, plan);
    }

    // 이미 해당 Plan과 userId로 공동작업자로 추가되어있는지 확인
    // coworker 테이블에 추가
    private Coworker addCoworker(User user, Plan plan) {
        if (coworkerRepository.existsByUserIdAndPlanId(user.getId(), plan.getId())) {
            throw new CommonException(ErrorEnum.BAD_REQUEST);
        }

        Coworker coworker = Coworker.builder()
                .user(user)
                .plan(plan)
                .build();

        coworkerRepository.save(coworker);

        return coworker;
    }
}
