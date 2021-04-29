package com.demoim_backend.demoim_backend.service;

import com.demoim_backend.demoim_backend.config.auth.PrincipalDetails;
import com.demoim_backend.demoim_backend.dto.UserUpdateProfileRequestDto;
import com.demoim_backend.demoim_backend.dto.UserUpdateProfileSaveDto;
import com.demoim_backend.demoim_backend.model.User;
import com.demoim_backend.demoim_backend.repository.UserRepository;
import com.demoim_backend.demoim_backend.s3.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FileUploadService fileUploadService;

    // 현재 로그인 한 유저 정보 반환
    public Optional<User> findCurUser(Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Long userId = principalDetails.getUser().getId();
        System.out.println(userId);
        return userRepository.findById(userId);
    }

    // 특정 유저 정보 반환
    public User findTargetUser(Long userId){
        return userRepository.findById(userId).orElseThrow(
                ()-> new IllegalArgumentException("해당 유저 정보가 없습니다")
        );
    }

    // 유저 정보 수정
    @Transactional
    public User updateProfile(Authentication authentication, UserUpdateProfileRequestDto userUpdateProfileDto, MultipartFile file){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Long userId = principalDetails.getUser().getId();
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new IllegalArgumentException("해당 유저 정보가 없습니다")
        );
        String profileImage;
        if (file == null){
            profileImage = user.getProfileImage();
        }
        else {
            profileImage =  fileUploadService.uploadImage(file);
        }

        UserUpdateProfileSaveDto userUpdateProfileSaveDto = new UserUpdateProfileSaveDto();
        userUpdateProfileSaveDto.setNickname(userUpdateProfileDto.getNickname());
        userUpdateProfileSaveDto.setPosition(userUpdateProfileDto.getPosition());
        userUpdateProfileSaveDto.setDesc(userUpdateProfileDto.getDesc());
        userUpdateProfileSaveDto.setProfileImage(profileImage);
        user.update(userUpdateProfileSaveDto);
        return user;
    }

    @Transactional
    public User updateProfileImg(Authentication authentication, MultipartFile file) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Long userId = principalDetails.getUser().getId();
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("해당 유저 정보가 없습니다")
        );
        String profileImage = fileUploadService.uploadImage(file);
        user.updateImg(profileImage);
        return user;
    }
}
