package com.demoim_backend.demoim_backend.service;

import com.demoim_backend.demoim_backend.dto.ExhibitionDto;
import com.demoim_backend.demoim_backend.dto.ExhibitionResponseDto;
import com.demoim_backend.demoim_backend.model.Exhibition;
import com.demoim_backend.demoim_backend.model.User;
import com.demoim_backend.demoim_backend.repository.ExhibitionRepository;
import com.demoim_backend.demoim_backend.s3.FileUploadService;
import org.json.JSONObject;
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


    // exhibition 존재여부 조회
    public Exhibition findExhibition(Long exhibitionId) {
        return exhibitionRepository.findById(exhibitionId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시물이 없습니다.")
        );
    }

    // exhibition 생성
    public ExhibitionResponseDto createExhibition(Authentication authentication, String requestBody, MultipartFile file) {
        ExhibitionDto exhibitionDto = new ExhibitionDto();
        JSONObject jsonObject = new JSONObject(requestBody);
        exhibitionDto.setTitle(jsonObject.getString("title"));
        exhibitionDto.setContents(jsonObject.getString("contents"));
        if (file == null) {
            exhibitionDto.setThumbnail(null);
        } else {
            exhibitionDto.setThumbnail(fileUploadService.uploadImage(file));
        }


        User user = userService.findCurUser(authentication).orElseThrow(
                () -> new IllegalArgumentException("해당 회원이 존재하지않습니다.")
        );

        Exhibition exhibition = exhibitionDto.toEntity();
        exhibition.setUser(user);

        Exhibition exhibitionResponse = exhibitionRepository.save(exhibition);

        ExhibitionResponseDto exhibitionResponseDto = new ExhibitionResponseDto();
        return exhibitionResponseDto.entityToDto(exhibitionResponse);
    }


    // 이미지 저장.
    public String createExhibitionImg(Authentication authentication, MultipartFile file) {
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
    public ExhibitionResponseDto getExhibition(Long exhibitionId) {

        //exhibition 조회
        Exhibition exhibition = this.findExhibition(exhibitionId);

        //Dto에 넣어서 반환
        ExhibitionResponseDto exhibitionResponseDto = new ExhibitionResponseDto();
        return exhibitionResponseDto.entityToDto(exhibition);
    }

    // exhibition 수정
    @Transactional
    public ExhibitionResponseDto updateExhibition(Authentication authentication,
                                                  String requestBody, MultipartFile file,
                                                  Long exhibitionId) {

        ExhibitionDto exhibitionDto = new ExhibitionDto();
        JSONObject jsonObject = new JSONObject(requestBody);
        exhibitionDto.setTitle(jsonObject.getString("title"));
        exhibitionDto.setContents(jsonObject.getString("contents"));

        ExhibitionResponseDto exhibitionResponseDto;
        String thumbnail = "";

        //user 조회
        User user = userService.findCurUser(authentication).orElseThrow(
                () -> new IllegalArgumentException("해당 회원이 존재하지않습니다")
        );

        //Exhibition 존재 여부 확인
        Exhibition exhibition = this.findExhibition(exhibitionId);


        // 작성자와 로그인한 같다면
        if (user.getId() == exhibition.getExhibitionUser().getId()) {

            if (file == null) {
                exhibitionDto.setThumbnail(exhibition.getThumbnail());
            }else {
                exhibitionDto.setThumbnail(fileUploadService.uploadImage(file));
            }
            exhibition.update(exhibitionDto);
            exhibition.setUser(user);

            Exhibition exhibitionReponse = exhibitionRepository.save(exhibition);
            exhibitionResponseDto = new ExhibitionResponseDto();

            return exhibitionResponseDto.entityToDto(exhibitionReponse);

        } else {
            exhibitionResponseDto = null;
            return exhibitionResponseDto;
        }
    }

    @Transactional
    public ExhibitionResponseDto updateExhibitionImg(Authentication authentication, Long exhibitionId, MultipartFile file) {
        ExhibitionResponseDto exhibitionResponseDto;
        //user 조회
        User user = userService.findCurUser(authentication).orElseThrow(
                () -> new IllegalArgumentException("해당 회원이 존재하지않습니다")
        );

        //Exhibition 존재 여부 확인
        Exhibition exhibition = this.findExhibition(exhibitionId);

        // 작성자와 로그인한 같다면
        if (user.getId() == exhibition.getExhibitionUser().getId()) {

            String thumbnail = fileUploadService.uploadImage(file);
            exhibition.updateImg(thumbnail);
            exhibition.setUser(user);

            exhibitionResponseDto = new ExhibitionResponseDto();

            return exhibitionResponseDto.entityToDto(exhibition);

        } else {
            throw new IllegalArgumentException("게시글 작성자가 아닙니다.");
        }

    }


    // exhibition 삭제
    public String deleteExhibition(Authentication authentication, Long exhibitionId) {

        //user 조회
        User user = userService.findCurUser(authentication).orElseThrow(
                () -> new IllegalArgumentException("해당 회원이 존재하지않습니다")
        );

        //Exhibition 존재 여부 확인
        Exhibition exhibition = this.findExhibition(exhibitionId);

        //user와 Exhibition 작성자가 일치한다면
        if (user.getId() == exhibition.getExhibitionUser().getId()) {
            user.getExhibitions().remove(exhibition);
            exhibitionRepository.deleteById(exhibitionId);

            return "Success";
        } else {
            return "fail";
        }
    }


}
