package com.sparta.restplaceforj.service;

import com.sparta.restplaceforj.dto.UserSignUpRequestDto;
import com.sparta.restplaceforj.dto.UserSignUpResponseDto;
import com.sparta.restplaceforj.entity.User;
import com.sparta.restplaceforj.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public UserSignUpResponseDto createUser(UserSignUpRequestDto requestDto) {

    // 이메일 중복 검사
    userRepository.findByEmailOrThrow(requestDto.getEmail());

    // 닉네임 중복 검사
    userRepository.findByNicknameOrThrow(requestDto.getNickname());

    // 비밀번호 암호화
    String password = passwordEncoder.encode(requestDto.getPassword());

    // User DB에 저장
    User user = User.builder()
        .email(requestDto.getEmail())
        .password(password)
        .name(requestDto.getName())
        .nickname(requestDto.getNickname())
        .build();

    userRepository.save(user);

    UserSignUpResponseDto userSignUpResponseDto = UserSignUpResponseDto.builder()
        .user(user)
        .build();

    return userSignUpResponseDto;
  }
}
