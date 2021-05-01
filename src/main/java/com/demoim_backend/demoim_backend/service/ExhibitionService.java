package com.demoim_backend.demoim_backend.service;

import com.demoim_backend.demoim_backend.dto.ExhibitionDto;
import com.demoim_backend.demoim_backend.dto.ExhibitionResponseDto;
import com.demoim_backend.demoim_backend.model.Exhibition;
import com.demoim_backend.demoim_backend.model.User;
import com.demoim_backend.demoim_backend.repository.ExhibitionRepository;
import com.demoim_backend.demoim_backend.s3.FileUploadService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


@Service
public class ExhibitionService {

    private final ExhibitionRepository exhibitionRepository;
    private final UserService userService;
    private final FileUploadService fileUploadService;

    public ExhibitionService(ExhibitionRepository exhibitionRepository, UserService userService, FileUploadService fileUploadService) {
        this.exhibitionRepository = exhibitionRepository;
        this.userService = userService;
        this.fileUploadService = fileUploadService;
    }


    // exhibition 단건 조회
    public Exhibition findExhibition(Long exhibitionId){
        return exhibitionRepository.findById(exhibitionId).orElseThrow(
                ()-> new IllegalArgumentException("해당 게시물이 없습니다.")
        );
    }

    // exhibition 생성
    public ExhibitionResponseDto createExhibition(Authentication authentication, ExhibitionDto exhibitionDto,MultipartFile file) {

        String thumbnail;

        User user = userService.findCurUser(authentication).orElseThrow(
                () -> new IllegalArgumentException("해당 회원이 존재하지않습니다.")
        );

        Exhibition exhibition = exhibitionDto.toEntity();
        exhibition.setUser(user);

        Exhibition exhibitionResponse = exhibitionRepository.save(exhibition);

        ExhibitionResponseDto exhibitionResponseDto = new ExhibitionResponseDto();
        return exhibitionResponseDto.entityToDto(exhibitionResponse);
    }


//    // 이미지 저장 버젼 2
//    @Transactional
//    public ExhibitionResponseDto createExhibitionImg(Authentication authentication,Long exhibitionId,MultipartFile file){
//        //user 조회
//        User user = userService.findCurUser(authentication).orElseThrow(
//                () -> new IllegalArgumentException("해당 회원이 존재하지않습니다")
//        );
//
//        //Exhibition 존재 여부 확인
//        Exhibition exhibition = this.findExhibition(exhibitionId);
//
//        String thumbnail = fileUploadService.uploadImage(file);
//        exhibition.updateImg(thumbnail);
//
//        ExhibitionResponseDto exhibitionResponseDto = new ExhibitionResponseDto();
//        exhibitionResponseDto.entityToDto(exhibition);
//
//        return exhibitionResponseDto;
//    }

     // 이미지 저장.
    public String createExhibitionImg(Authentication authentication ,MultipartFile file){
        //user 조회
        User user = userService.findCurUser(authentication).orElseThrow(
                () -> new IllegalArgumentException("해당 회원이 존재하지않습니다")
        );
        String thumbnail = fileUploadService.uploadImage(file);

        return thumbnail;
    }


    // exhibition 리스트 조회
    public List<ExhibitionResponseDto> getExhibitionList(int page, int size) {

        Page<Exhibition> pageExhibition = exhibitionRepository.findAll(PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt")));

        // 들고온 Entity를 Dto에 넣어서 반환하는 코드 70-78
        List<ExhibitionResponseDto> exhibitionResponseDtoList = new ArrayList<>();
        ExhibitionResponseDto exhibitionResponseDto = new ExhibitionResponseDto();

        List<Exhibition> exhibitionEntity = pageExhibition.getContent();

        for (Exhibition exhibition : exhibitionEntity) {
            exhibitionResponseDtoList.add(exhibitionResponseDto.entityToDto(exhibition));
        }

        return exhibitionResponseDtoList;
    }


    // exhibition 단일 조회
    public ExhibitionResponseDto getExhibition(Long exhibitionId){

        //exhibition 조회
        Exhibition exhibition = this.findExhibition(exhibitionId);

        //Dto에 넣어서 반환
        ExhibitionResponseDto exhibitionResponseDto = new ExhibitionResponseDto();
        return exhibitionResponseDto.entityToDto(exhibition);
    }

    // exhibition 수정
    @Transactional
    public ExhibitionResponseDto updateExhibition(Authentication authentication,
                                                  ExhibitionDto exhibitionDto,
                                                  Long exhibitionId,
                                                  MultipartFile file){

        ExhibitionResponseDto exhibitionResponseDto;
        String thumbnail = "";

        //user 조회
        User user = userService.findCurUser(authentication).orElseThrow(
                () -> new IllegalArgumentException("해당 회원이 존재하지않습니다")
        );

        //Exhibition 존재 여부 확인
        Exhibition exhibition = this.findExhibition(exhibitionId);

        if(file == null){
            thumbnail = exhibition.getThumbnail();

        }else{
            thumbnail = fileUploadService.uploadImage(file);
        }

        // 작성자와 로그인한 같다면
        if(user.getId() == exhibition.getExhibitionUser().getId()){

            //update
            exhibitionDto.setThumbnail(thumbnail);
            exhibition.update(exhibitionDto);
            exhibition.setUser(user);

            Exhibition exhibitionReponse = exhibitionRepository.save(exhibition);
            exhibitionResponseDto = new ExhibitionResponseDto();

            return exhibitionResponseDto.entityToDto(exhibitionReponse);

        }else{
            exhibitionResponseDto = null;
            return exhibitionResponseDto;
        }
    }

    @Transactional
    public ExhibitionResponseDto updateExhibitionImg(Authentication authentication,Long exhibitionId,MultipartFile file){
        //user 조회
        User user = userService.findCurUser(authentication).orElseThrow(
                () -> new IllegalArgumentException("해당 회원이 존재하지않습니다")
        );

        //Exhibition 존재 여부 확인
        Exhibition exhibition = this.findExhibition(exhibitionId);

        String thumbnail = fileUploadService.uploadImage(file);
        exhibition.updateImg(thumbnail);

        ExhibitionResponseDto exhibitionResponseDto = new ExhibitionResponseDto();
        exhibitionResponseDto.entityToDto(exhibition);

        return exhibitionResponseDto;
    }




    // exhibition 삭제
    public String deleteExhibition(Authentication authentication,Long exhibitionId){

        //user 조회
        User user = userService.findCurUser(authentication).orElseThrow(
                () -> new IllegalArgumentException("해당 회원이 존재하지않습니다")
        );

        //Exhibition 존재 여부 확인
        Exhibition exhibition = this.findExhibition(exhibitionId);

        //user와 Exhibition 작성자가 일치한다면
        if(user.getId() == exhibition.getExhibitionUser().getId()){
            user.getExhibitions().remove(exhibition);
            exhibitionRepository.deleteById(exhibitionId);

            return "Seccuess";
        }else{
            return "fail";
        }
    }


}
