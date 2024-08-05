package com.sparta.restplaceforj.service;

import com.sparta.restplaceforj.dto.UserResignResponseDto;
import com.sparta.restplaceforj.dto.UserSignUpRequestDto;
import com.sparta.restplaceforj.dto.UserSignUpResponseDto;
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

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Service s3Service;

    /**
     * 회원 가입 메서드
     *
     * @param userSignUpRequestDto 필드명: email, password, name, nickname
     * @return userSignUpResponseDto 필드명: email, name, nickname;
     */
    @Transactional
    public UserSignUpResponseDto createUser(UserSignUpRequestDto userSignUpRequestDto) {

        if (userRepository.existsByEmail(userSignUpRequestDto.getEmail())) {
            throw new CommonException(ErrorEnum.DUPLICATED_EMAIL);
        }

        if (userRepository.existsByNickname(userSignUpRequestDto.getNickname())) {
            throw new CommonException(ErrorEnum.DUPLICATED_NICKNAME);
        }

        String password = passwordEncoder.encode(userSignUpRequestDto.getPassword());

        User user = User.builder()
                .email(userSignUpRequestDto.getEmail())
                .password(password)
                .name(userSignUpRequestDto.getName())
                .nickname(userSignUpRequestDto.getNickname())
                .build();

        userRepository.save(user);

        return UserSignUpResponseDto.builder()
                .user(user)
                .build();
    }

    /**
     * 회원 탈퇴 메서드
     *
     * @param user
     * @param password
     * @return userResignResponseDto : email, role
     */
    @Transactional
    public UserResignResponseDto deleteUser(User user, String password){
      if(user.getUserStatus() == UserStatus.DEACTIVATE) {
          throw new CommonException(ErrorEnum.BAD_REQUEST);
      }

      if(!passwordEncoder.matches(password, user.getPassword())) {
        throw new CommonException(ErrorEnum.BAD_PASSWORD);
      }

      user.setUserStatus(UserStatus.DEACTIVATE);
      userRepository.save(user);

      return UserResignResponseDto.builder()
              .email(user.getEmail())
              .role(user.getUserRole())
              .build();
    }

    /**
     * 유저 프로필 조회 메서드
     *
     * @param userId
     * @return UserProfileResponseDto :nickname, bio, profilePicture
     */
    public UserProfileResponseDto getUserProfile(Long userId) {
        User user = userRepository.findByIdOrThrow(userId);

        return UserProfileResponseDto.builder()
                .profileImage(user.getProfileImage())
                .bio(user.getBio())
                .nickname(user.getNickname())
                .build();
    }

    /**
     * 유저 프로필 사진 업로드 메서드
     *
     * @param multipartFile
     * @param user
     * @return UpdateUserProfileImageResponseDto : profileImageUrl
     */
    @Transactional
    public UpdateUserProfileImageResponseDto updateUserProfileImage(MultipartFile multipartFile, User user, Long userId) throws IOException {

        if(user.getId() != userId) throw new CommonException(ErrorEnum.INVALID_ACCESS);

        String fileName = s3Service.upload(multipartFile);
        user.setProfileImage(fileName);
        userRepository.save(user);

        return UpdateUserProfileImageResponseDto.builder()
                .profileImage(fileName)
                .build();
    }


    /**
     * 유저 프로필 수정 메서드
     *
     * @param userUpdateRequestDto : nickname, bio, currentPassword, newPassword, confirmPassword;
     * @param user
     * @param userId
     * @return UserProfileResponseDto :nickname, bio, profilePicture
     */
    @Transactional
    public UserProfileResponseDto updateUserProfile(UserUpdateRequestDto userUpdateRequestDto, User user, Long userId) {

        if(user.getId() != userId) throw new CommonException(ErrorEnum.INVALID_ACCESS);

        // 변경할 값이 있는지 확인하고 업데이트
        String nickname = userUpdateRequestDto.getNickname() != null ? userUpdateRequestDto.getNickname() : user.getNickname();
        String bio = userUpdateRequestDto.getBio() != null ? userUpdateRequestDto.getBio() : user.getBio();
        String password = user.getPassword();

        // 중복된 닉네임을 가진 사용자가 있을 시 예외처리
        if(userRepository.existsByNickname(nickname)) throw new CommonException(ErrorEnum.DUPLICATED_NICKNAME);

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
