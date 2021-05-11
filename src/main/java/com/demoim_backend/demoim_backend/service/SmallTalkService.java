package com.demoim_backend.demoim_backend.service;

import com.demoim_backend.demoim_backend.dto.CommentResponseDto;
import com.demoim_backend.demoim_backend.dto.ResponseUserDto;
import com.demoim_backend.demoim_backend.dto.SmallTalkDto;
import com.demoim_backend.demoim_backend.dto.SmallTalkResponseDto;
import com.demoim_backend.demoim_backend.model.SmallTalk;
import com.demoim_backend.demoim_backend.model.User;
import com.demoim_backend.demoim_backend.repository.SmallTalkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;



@Service
@RequiredArgsConstructor
public class SmallTalkService {

    private final SmallTalkRepository smallTalkRepository;
    private final UserService userService;
    private final CommentService commentService;



    // SmallTalk 엔티티를 DTo로 담아주는 메소드
    public SmallTalkResponseDto entityToDto(SmallTalk smallTalk) {

        ResponseUserDto responseUserDto = new ResponseUserDto();
        List<CommentResponseDto> commentList = commentService.getCommentForSmallTalk(smallTalk.getId());
        System.out.println("commentList :" + commentList);
        return SmallTalkResponseDto.builder()
                .id(smallTalk.getId())
                .contents(smallTalk.getContents())
                .user(responseUserDto.entityToDto(smallTalk.getSmallTalkUser()))
                .commentList(commentList)
                .createdAt(smallTalk.getCreatedAt())
                .modifiedAt(smallTalk.getModifiedAt())
                .build();
    }

    // 해당 smallTalk의 존재여부 확인
    public SmallTalk findSmallTalk(Long smallTalkId){
        return smallTalkRepository.findById(smallTalkId).orElseThrow(
                ()->new IllegalArgumentException("해당 피드가 존재하지않습니다.")
        );
    }


    //smallTalk 생성
    public SmallTalkResponseDto createSmallTalk(Authentication authentication, SmallTalkDto smallTalkDto) {

        User user = userService.findCurUser(authentication).orElseThrow(
                ()-> new IllegalArgumentException("해당 회원이 존재하지않습니다.")
        );

        SmallTalk smallTalk = SmallTalk.builder()
                .contents(smallTalkDto.getContents())
                .build();

        //양방향 매핑을 위해 객체 참조
        smallTalk.setUser(user);
        // DB에 저장
        SmallTalk smallTalkResponse = smallTalkRepository.save(smallTalk);

        //Dto에 담아서 반환.
        SmallTalkResponseDto responseDto = this.entityToDto(smallTalkResponse);

        return responseDto;
    }


    //smallTalk List 조회
    public List<SmallTalkResponseDto> getSmallTalkList(int page, int size) {

        Page<SmallTalk> pageSmallTalk = smallTalkRepository.findAll(PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt")));

        // 페이징한 Page<SmallTalk>를 Dto에 담아서 반환하기 위한 코드 (77~83)
        List<SmallTalk> smallTalkEntitys = pageSmallTalk.getContent();
        List<SmallTalkResponseDto> smallTalkListResponseDtoList = new ArrayList<>();

        for (SmallTalk smallTalk : smallTalkEntitys) {
            smallTalkListResponseDtoList.add(this.entityToDto(smallTalk));
        }

        return smallTalkListResponseDtoList;
    }

    // 수정
    @Transactional
    public SmallTalkResponseDto updateSmallTalk(Authentication authentication, SmallTalkDto smallTalkDto, Long smallTalkId) {

        SmallTalkResponseDto responseDto;

        // user객체 가져옴
        User user = userService.findCurUser(authentication).orElseThrow(
                ()-> new IllegalArgumentException("해당 회원이 존재하지않습니다.")
        );

        // SmallTalk 존재 여부 확인
        SmallTalk smallTalk = this.findSmallTalk(smallTalkId);


        // user와 smallTalk작성자가 일치한다면
        if (user.getId() == smallTalk.getSmallTalkUser().getId()) {
            smallTalk.Update(smallTalkDto);
            smallTalk.setUser(user);

            responseDto = this.entityToDto(smallTalk);
            return responseDto;

        } else {
            responseDto = null;
            return responseDto;
        }
    }

    // 삭제
    public String deleteSmallTalk(Authentication authentication, Long smallTalkId) {

        // user객체 가져옴
        User user = userService.findCurUser(authentication).orElseThrow(
                ()-> new IllegalArgumentException("해당 회원이 존재하지않습니다.")
        );

        // SmallTalk 존재 여부 확인
        SmallTalk smallTalk = this.findSmallTalk(smallTalkId);

        // user와 smallTalk작성자가 일치한다면
        if(user.getId() == smallTalk.getSmallTalkUser().getId()){
            user.getSmallTalks().remove(smallTalk);
            smallTalkRepository.deleteById(smallTalkId);
            return "Success";

        }else{
            return "fail";
        }
    }

    // 로그인된 유저의 스몰토크 조회
    public List<SmallTalkResponseDto> findMySmallTalk(Authentication authentication){

        User user = userService.findCurUser(authentication).orElseThrow(
                ()-> new IllegalArgumentException("해당 회원이 존재하지않습니다.")
        );
        Long userId = user.getId();

        List<SmallTalk> smallTalk = smallTalkRepository.findAllBySmallTalkUserId(userId);

        List<SmallTalkResponseDto> smallTalkListResponseDtoList = new ArrayList<>();

        for (SmallTalk smallTalk1 : smallTalk) {
            smallTalkListResponseDtoList.add(this.entityToDto(smallTalk1));
        }

        return smallTalkListResponseDtoList;

    }
}