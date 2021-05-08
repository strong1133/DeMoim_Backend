package com.demoim_backend.demoim_backend.controller;

import com.demoim_backend.demoim_backend.dto.ExhibitionDto;
import com.demoim_backend.demoim_backend.dto.ExhibitionResponseDto;
import com.demoim_backend.demoim_backend.service.ExhibitionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;


@RestController
public class ExhibitionController {

    private final ExhibitionService exhibitionService;

    public ExhibitionController(ExhibitionService exhibitionService) {
        this.exhibitionService = exhibitionService;
    }

    // exhibition 작성
    @PostMapping("/api/exhibition")
    public ExhibitionResponseDto createExhibition(Authentication authentication, @RequestPart(value = "requestBody") String requestBody, @RequestPart MultipartFile file) {
        return exhibitionService.createExhibition(authentication,requestBody,file);
    }

    // exhibtion 이미지 작성
    @PostMapping("/api/exhibitionImg")
    public String createExhibitionImg(Authentication authentication, @RequestPart MultipartFile file) {
        return exhibitionService.createExhibitionImg(authentication, file);
    }

    // exhibition 조회
    @GetMapping("/api/exhibition")
    public List<ExhibitionResponseDto> getExhibitionList(@RequestParam int page, int size) {
        return exhibitionService.getExhibitionList(page, size);
    }

    // exhibition 단건 조회
    @GetMapping("/api/exhibition/detail")
    public ExhibitionResponseDto getExhibition(@RequestParam(name = "exhibition_id") Long exhibitionId) {
        return exhibitionService.getExhibition(exhibitionId);
    }

    // exhibition 수정
    @PutMapping("/api/exhibition/detail")
    public ExhibitionResponseDto updateExhibition(Authentication authentication,
                                                  @RequestPart(value = "requestBody") String requestBody,
                                                  @RequestPart(required = false) MultipartFile file,
                                                  @RequestParam(name = "exhibition_id") Long exhibitionId) {

        ExhibitionResponseDto exhibition = exhibitionService.updateExhibition(authentication, requestBody, file, exhibitionId);

        if (exhibition == null) {
            throw new IllegalArgumentException("게시글 작성자가 아닙니다.");

        } else {
            return exhibition;
        }
    }

    // exhibtion 이미지 수정
    @PutMapping("/api/exhibition/detailImg")
    public ExhibitionResponseDto updateExhibitionImg(Authentication authentication,
                                                     @RequestParam(name = "exhibition_id") Long exhibitionId,
                                                     @RequestPart MultipartFile file) {
        return exhibitionService.updateExhibitionImg(authentication, exhibitionId, file);
    }

    // exhibition 삭제
    @DeleteMapping("/api/exhibition/detail")
    public ResponseEntity deleteExhibition(Authentication authentication, @RequestParam(name = "exhibition_id") Long exhibitionId) {

        String result = exhibitionService.deleteExhibition(authentication, exhibitionId);
        HashMap<String, String> message;

        //삭제 성공 시
        if (result.equals("Success")) {
            message = new HashMap<>();
            message.put("msg", result);
            return new ResponseEntity<>(message, HttpStatus.OK);

            //삭제 실패 시
        } else {
            message = new HashMap<>();
            message.put("msg", result);
            return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

}
