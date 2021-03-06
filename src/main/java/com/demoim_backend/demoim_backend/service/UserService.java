package com.demoim_backend.demoim_backend.service;

import com.demoim_backend.demoim_backend.config.auth.PrincipalDetails;
import com.demoim_backend.demoim_backend.dto.*;
import com.demoim_backend.demoim_backend.model.ApplyInfo;
import com.demoim_backend.demoim_backend.model.Comment;
import com.demoim_backend.demoim_backend.model.Team;
import com.demoim_backend.demoim_backend.model.User;
import com.demoim_backend.demoim_backend.repository.ApplyInfoRepository;
import com.demoim_backend.demoim_backend.repository.TeamRepository;
import com.demoim_backend.demoim_backend.repository.UserRepository;
import com.demoim_backend.demoim_backend.s3.FileUploadService;
import com.demoim_backend.demoim_backend.util.NowTeamCount;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FileUploadService fileUploadService;
    private final TeamRepository teamRepository;
    private final ApplyInfoRepository applyInfoRepository;
    private final NowTeamCount nowTeamCount;

    // response 객체로 만들어주는 매서드
    public MypageResponseDto entityToDto(User user, List<Team> team) {
        List<ApplyInfo> applyInfos = applyInfoRepository.findTeamIdByUserIdAndMembershipAndApplyState(user.getId(), ApplyInfo.Membership.MEMBER, ApplyInfo.ApplyState.WAITING);
        List<Long> applyInfoList = new ArrayList<>();
        for(ApplyInfo applyInfo : applyInfos){
            applyInfoList.add(applyInfo.getTeam().getId());
        }
        int nowTeamCnt = nowTeamCount.nowTeamCnt(user);
        return new MypageResponseDto(user, nowTeamCnt, applyInfoList, team);
    }

    // 현재 로그인 한 유저 정보 반환
    public Optional<User> findCurUser(Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Long userId = principalDetails.getUser().getId();
        System.out.println("UserService의 :" + userId);
        return userRepository.findById(userId);
    }

    public User findMyUserInfo(Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Long userId = principalDetails.getUser().getId();
        return userRepository.findById(userId).orElseThrow(
                ()-> new IllegalArgumentException("해당 유저가 없습니다.")
        );
    }

    // 현재 로그인 한 유저 정보 반환22
    public MypageResponseDto mypageUserInfo (Authentication authentication){
        User user = findMyUserInfo(authentication);
        List<Team> team = teamRepository.findByLeader(user);

        return entityToDto(user,team);
    }


    // 특정 유저 정보 반환
    public User findTargetUser(Long userId){
        return userRepository.findById(userId).orElseThrow(
                ()-> new IllegalArgumentException("해당 유저 정보가 없습니다")
        );
    }

    public MypageResponseDto findTargetUserDto(Long userId){
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new IllegalArgumentException("해당 유저 정보가 없습니다.")
        );
        List<Team> team = teamRepository.findByLeader(user);
        return entityToDto(user,team);
    }

    // 유저 정보 수정
    @Transactional
    public User updateProfile(Authentication authentication, String userEditInfo, MultipartFile file){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Long userId = principalDetails.getUser().getId();
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new IllegalArgumentException("해당 유저 정보가 없습니다")
        );
        System.out.println("=1=");
        Optional<ApplyInfo> applyInfo = applyInfoRepository.findTopByUserId(user.getId());
        System.out.println("applyInfo :" + applyInfo);

        String profileImage;
        if (file == null){
            profileImage = user.getProfileImage();
        }
        else {
            profileImage =  fileUploadService.uploadImage(file);
        }
        JSONObject jsonObject = new JSONObject(userEditInfo);
        UserUpdateProfileRequestDto userUpdateProfileRequestDto =new UserUpdateProfileRequestDto();
        String nickname = jsonObject.getString("nickname");
        String position=jsonObject.getString("position");
        String description=jsonObject.getString("description");

        if (applyInfo.isPresent()){
            if (!position.equals(user.getPosition())){
                throw new IllegalArgumentException("지원 내역이나 팀 활동 내역이 있어 포지션 변경이 불가합니다!");
            }
        }

        userUpdateProfileRequestDto.setNickname(nickname);
        userUpdateProfileRequestDto.setDescription(description);
        userUpdateProfileRequestDto.setPosition(position);


        UserUpdateProfileSaveRequestDto userUpdateProfileSaveDto = new UserUpdateProfileSaveRequestDto();
        userUpdateProfileSaveDto.setNickname(userUpdateProfileRequestDto.getNickname());
        userUpdateProfileSaveDto.setPosition(userUpdateProfileRequestDto.getPosition());
        userUpdateProfileSaveDto.setDescription(userUpdateProfileRequestDto.getDescription());
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
