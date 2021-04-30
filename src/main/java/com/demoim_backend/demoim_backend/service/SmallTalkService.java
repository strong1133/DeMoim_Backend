package com.demoim_backend.demoim_backend.service;

import com.demoim_backend.demoim_backend.config.auth.PrincipalDetails;
import com.demoim_backend.demoim_backend.dto.SmallTalkDto;
import com.demoim_backend.demoim_backend.dto.SmallTalkResponseDto;
import com.demoim_backend.demoim_backend.model.SmallTalk;
import com.demoim_backend.demoim_backend.model.User;
import com.demoim_backend.demoim_backend.repository.SmallTalkRepository;
import com.demoim_backend.demoim_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class SmallTalkService {

    private SmallTalkRepository smallTalkRepository;
    @Autowired
    private UserRepository userRepository;

    public SmallTalkService(SmallTalkRepository smallTalkRepository) {
        this.smallTalkRepository = smallTalkRepository;
    }


    // SmallTalk 엔티티를 DTo로 담아주는 메소드
    public SmallTalkResponseDto toEntity(SmallTalk smallTalk) {
        return SmallTalkResponseDto.builder()
                .id(smallTalk.getId())
                .contents(smallTalk.getContents())
                .user(smallTalk.getSmallTalkUser())
                .createdAt(smallTalk.getCreatedAt())
                .modifiedAt(smallTalk.getModifiedAt())
                .build();
    }


    // 영속성 연결을 위해서 User객체 조회하는 메소드
    public User getUser(Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        // User 객체 조회
        User user = userRepository.findById(principalDetails.getUser().getId()).orElseThrow(
                () -> new IllegalArgumentException("회원이 존재하지않습니다.")
        );
        return user;
    }


    //smallTalk 생성
    public SmallTalkResponseDto createSmallTalk(Authentication authentication, SmallTalkDto smallTalkDto) {

        User user = this.getUser(authentication);

        SmallTalk smallTalk = SmallTalk.builder()
                .contents(smallTalkDto.getContents())
                .build();

        //양방향 매핑을 위해 객체 참조
        smallTalk.setUser(user);
        // DB에 저장
        SmallTalk smallTalkResponse = smallTalkRepository.save(smallTalk);

        //Dto에 담아서 반환.
        SmallTalkResponseDto responseDto = this.toEntity(smallTalkResponse);

        return responseDto;
    }


    //smallTalk List 조회
    public List<SmallTalkResponseDto> getSmallTalkList(int page, int size) {

        Page<SmallTalk> pageSmallTalk = smallTalkRepository.findAll(PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt")));

        // 페이징한 Page<SmallTalk>를 Dto에 담아서 반환하기 위한 코드 (77~83)
        List<SmallTalk> smallTalkEntitys = pageSmallTalk.getContent();
        List<SmallTalkResponseDto> smallTalkListResponseDtoList = new ArrayList<>();

        for (SmallTalk smallTalk : smallTalkEntitys) {
            smallTalkListResponseDtoList.add(this.toEntity(smallTalk));
        }

        return smallTalkListResponseDtoList;
    }

    // 수정
    @Transactional
    public SmallTalkResponseDto updateSmallTalk(Authentication authentication, SmallTalkDto smallTalkDto, Long smallTalkId) {

        SmallTalkResponseDto responseDto;

        // user객체 가져옴
        User user = this.getUser(authentication);

        // SmallTalk 존재 여부 확인
        SmallTalk smallTalk = smallTalkRepository.findById(smallTalkId).orElseThrow(
                () -> new IllegalArgumentException("해당 피드가 존재하지않습니다.")
        );

        // user와 smallTalk작성자가 일치한다면
        if (user.getId() == smallTalk.getSmallTalkUser().getId()) {
            smallTalk.Update(smallTalkDto);
            smallTalk.setUser(user);
            SmallTalk smallTalkResponse = smallTalkRepository.save(smallTalk);

            responseDto = this.toEntity(smallTalkResponse);
            return responseDto;

        } else {
            responseDto = null;
            return responseDto;
        }
    }

    // 삭제
    public String deleteSmallTalk(Authentication authentication, Long smallTalkId) {
        // user객체 가져옴
        User user = this.getUser(authentication);
        // SmallTalk 존재 여부 확인
        SmallTalk smallTalk = smallTalkRepository.findById(smallTalkId).orElseThrow(
                () -> new IllegalArgumentException("해당 피드가 존재하지않습니다.")
        );

        // user와 smallTalk작성자가 일치한다면
        if (user.getId() == smallTalk.getSmallTalkUser().getId()) {
            smallTalkRepository.deleteById(smallTalkId);
            return "삭제 성공";

        } else {
            return "게시글 작성자가 아닙니다.";
        }
    }

    // 로그인된 유저의 스몰토크 조회
    public List<SmallTalkResponseDto> findMySmallTalk(Authentication authentication){
        User user = this.getUser(authentication);
        Long userId = user.getId();

        List<SmallTalk> smallTalk = smallTalkRepository.findAllBySmallTalkUserId(userId);
        List<SmallTalkResponseDto> smallTalkListResponseDtoList = new ArrayList<>();

        for (SmallTalk smallTalk1 : smallTalk) {
            smallTalkListResponseDtoList.add(this.toEntity(smallTalk1));
        }

        return smallTalkListResponseDtoList;

    }
}