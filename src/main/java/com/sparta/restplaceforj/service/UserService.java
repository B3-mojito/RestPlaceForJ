package com.sparta.restplaceforj.service;

import com.sparta.restplaceforj.dto.*;
import com.sparta.restplaceforj.entity.User;
import com.sparta.restplaceforj.entity.UserStatus;
import com.sparta.restplaceforj.exception.CommonException;
import com.sparta.restplaceforj.exception.ErrorEnum;
import com.sparta.restplaceforj.repository.UserRepository;
import com.sparta.restplaceforj.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Service s3Service;

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

        return UserSignUpResponseDto.builder()
                .user(user)
                .build();
    }

    public UserProfileResponseDto getUserProfile(Long userId) {
        User user = userRepository.findByIdOrThrow(userId);

        return UserProfileResponseDto.builder()
                .profilePicture(user.getProfilePicture())
                .bio(user.getBio())
                .nickname(user.getNickname())
                .build();
    }

    @Transactional
    public UpdateUserProfileImageResponseDto updateUserProfileImage(MultipartFile multipartFile, User user) throws IOException {
        String fileName = s3Service.upload(multipartFile);
        user.setProfilePicture(fileName);
        userRepository.save(user);

        return UpdateUserProfileImageResponseDto.builder()
                .profileImageUrl(fileName)
                .build();
    }

    @Transactional
    public UserProfileResponseDto updateUserProfile(UserUpdateRequestDto userUpdateRequestDto, User user) {

        // 변경할 값이 있는지 확인하고 업데이트
        String nickname = userUpdateRequestDto.getNickname() != null ? userUpdateRequestDto.getNickname() : user.getNickname();
        String bio = userUpdateRequestDto.getBio() != null ? userUpdateRequestDto.getBio() : user.getBio();
        String password = user.getPassword();

        // 비밀번호 변경 로직
        if (!userUpdateRequestDto.getCurrentPassword().isEmpty()
                && !userUpdateRequestDto.getNewPassword().isEmpty()
                && !userUpdateRequestDto.getConfirmPassword().isEmpty()) {

            // 현재 비밀번호가 올바른지 확인
            if (!passwordEncoder.matches(userUpdateRequestDto.getCurrentPassword(), password)) {
                throw new CommonException(ErrorEnum.BAD_PASSWORD);
            }

            // 새 비밀번호와 확인 비밀번호가 일치하는지 확인
            if (userUpdateRequestDto.getNewPassword().equals(userUpdateRequestDto.getConfirmPassword())) {
                password = passwordEncoder.encode(userUpdateRequestDto.getNewPassword());
            } else {
                throw new CommonException(ErrorEnum.BAD_PASSWORD);
            }
        }


        // 유저 정보 업데이트 및 저장
        user.updateProfile(nickname, bio, password);
        userRepository.save(user);

        // 응답 DTO 생성
        return UserProfileResponseDto.builder()
                .bio(user.getBio())
                .nickname(user.getNickname())
                .build();
    }
}
