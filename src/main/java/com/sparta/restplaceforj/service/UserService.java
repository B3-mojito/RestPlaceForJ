package com.sparta.restplaceforj.service;

import com.sparta.restplaceforj.dto.UserProfileResponseDto;
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
    public UserSignUpResponseDto createUser(UserSignUpRequestDto userSignUprequestDto) {

        if (userRepository.existsByEmail(userSignUprequestDto.getEmail())) {
            throw new CommonException(ErrorEnum.DUPLICATED_EMAIL);
        }

        if (userRepository.existsByNickname(userSignUprequestDto.getNickname())) {
            throw new CommonException(ErrorEnum.DUPLICATED_NICKNAME);
        }

        String password = passwordEncoder.encode(userSignUprequestDto.getPassword());

        User user = User.builder()
                .email(userSignUprequestDto.getEmail())
                .password(password)
                .name(userSignUprequestDto.getName())
                .nickname(userSignUprequestDto.getNickname())
                .build();

        userRepository.save(user);

        UserSignUpResponseDto userSignUpresponseDto = UserSignUpResponseDto.builder()
                .user(user)
                .build();

        return userSignUpresponseDto;
    }

    public UserProfileResponseDto getUserProfile(Long userId) {
        User user = userRepository.findByIdOrThrow(userId);
        UserProfileResponseDto userProfileResponseDto = UserProfileResponseDto.builder()
                .profilePicture(user.getProfilePicture())
                .bio(user.getBio())
                .nickname(user.getNickname())
                .build();

        return userProfileResponseDto;

    }
}
