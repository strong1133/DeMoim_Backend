package com.demoim_backend.demoim_backend.controller;

import com.demoim_backend.demoim_backend.dto.SmallTalkDto;
import com.demoim_backend.demoim_backend.dto.SmallTalkResponseDto;
import com.demoim_backend.demoim_backend.service.SmallTalkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;


@RestController
public class SmallTalkController {

    private SmallTalkService smallTalkService;

    public SmallTalkController(SmallTalkService smallTalkService) {
        this.smallTalkService = smallTalkService;
    }

    //smalltalk 생성
    @PostMapping("/api/smalltalk")
    public SmallTalkResponseDto createSmallTalk(Authentication authentication, @RequestBody @Validated SmallTalkDto smallTalkDto){
        return smallTalkService.createSmallTalk(authentication,smallTalkDto);
    }

    //smalltalk List 조회
    @GetMapping("/api/smalltalk")
    public List<SmallTalkResponseDto> getSmallTalkList(@RequestParam int page, int size){
//        System.out.println(page);
        return smallTalkService.getSmallTalkList(page, size);
    }

    //smalltalk 수정
    @PutMapping("/api/smalltalk/detail")
    public SmallTalkResponseDto updateSmalltalk(Authentication authentication, @Valid @RequestBody SmallTalkDto smallTalkDto, @RequestParam(name = "smalltalk_id")Long smallTalkId){
        SmallTalkResponseDto smallTalk= smallTalkService.updateSmallTalk(authentication,smallTalkDto,smallTalkId);

        if(smallTalk == null){
            throw new IllegalArgumentException("게시글 작성자가 아닙니다.");

        }else{
            return smallTalk;
        }
    }

    //smalltalk 삭제
    @DeleteMapping("/api/smalltalk/detail")
    public ResponseEntity deleteSmalltalk(Authentication authentication, @RequestParam(name = "smalltalk_id") Long smallTalkId){

        String result = smallTalkService.deleteSmallTalk(authentication,smallTalkId);
        HashMap<String, String> message;

        //삭제 성공 시
        if(result.equals("삭제 성공")){
            message = new HashMap<>();
            message.put("Success",result);
            return new ResponseEntity<>(message, HttpStatus.OK);

        //삭제 실패 시
        }else{
            message = new HashMap<>();
            message.put("Success", result);
            return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }



}