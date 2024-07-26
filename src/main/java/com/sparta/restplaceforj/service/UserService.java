package com.sparta.restplaceforj.service;

import com.sparta.restplaceforj.dto.UserSignUpRequestDto;
import com.sparta.restplaceforj.dto.UserSignUpResponseDto;
import com.sparta.restplaceforj.entity.User;
import com.sparta.restplaceforj.exception.CommonException;
import com.sparta.restplaceforj.exception.ErrorEnum;
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

    if (userRepository.existsByEmail(requestDto.getEmail())) {
      throw new CommonException(ErrorEnum.DUPLICATED_EMAIL);
    }

    if (userRepository.existsByNickname(requestDto.getNickname())) {
      throw new CommonException(ErrorEnum.DUPLICATED_NICKNAME);
    }

    String password = passwordEncoder.encode(requestDto.getPassword());

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
