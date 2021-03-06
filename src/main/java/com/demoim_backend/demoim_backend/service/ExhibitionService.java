package com.demoim_backend.demoim_backend.service;

import com.demoim_backend.demoim_backend.dto.ExhibitionDto;
import com.demoim_backend.demoim_backend.dto.ExhibitionResponseDto;
import com.demoim_backend.demoim_backend.model.Exhibition;
import com.demoim_backend.demoim_backend.model.User;
import com.demoim_backend.demoim_backend.repository.ExhibitionRepository;
import com.demoim_backend.demoim_backend.s3.FileUploadService;
import com.demoim_backend.demoim_backend.util.RandomImg;
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
import java.util.Random;


@Service
public class ExhibitionService {

    private final ExhibitionRepository exhibitionRepository;
    private final UserService userService;
    private final FileUploadService fileUploadService;
    private final RandomImg randomImg;

    public ExhibitionService(ExhibitionRepository exhibitionRepository, UserService userService, FileUploadService fileUploadService, RandomImg randomImg) {
        this.exhibitionRepository = exhibitionRepository;
        this.userService = userService;
        this.fileUploadService = fileUploadService;
        this.randomImg = randomImg;
    }


    // exhibition 존재여부 조회
    public Exhibition findExhibition(Long exhibitionId) {
        return exhibitionRepository.findById(exhibitionId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시물이 없습니다.")
        );
    }

    //json을 ExhibitionDto로 변환
    public ExhibitionDto jsonToExhibition(String requestBody) {
        JSONObject jsonObject = new JSONObject(requestBody);
        return ExhibitionDto.builder()
                .title(jsonObject.getString("title"))
                .contents(jsonObject.getString("contents"))
                .build();
    }


    // exhibition 생성
    public ExhibitionResponseDto createExhibition(Authentication authentication, String requestBody, MultipartFile file) {

        User user = userService.findCurUser(authentication).orElseThrow(
                () -> new IllegalArgumentException("해당 회원이 존재하지않습니다.")
        );

        ExhibitionDto exhibitionDto = jsonToExhibition(requestBody);

        //파일 이름 처리
        if (file == null) {
            Random random = new Random();
            int rNum = random.nextInt(15);
            exhibitionDto.setThumbnail(randomImg.rndImg(rNum));
        } else {
            exhibitionDto.setThumbnail(fileUploadService.uploadImage(file));
        }


        Exhibition exhibition = exhibitionDto.toEntity();
        exhibition.setUser(user);

        Exhibition exhibitionResponse = exhibitionRepository.save(exhibition);

        ExhibitionResponseDto exhibitionResponseDto = new ExhibitionResponseDto();
        return exhibitionResponseDto.entityToDto(exhibitionResponse);
    }


    // 이미지 저장.
    public String createExhibitionImg(Authentication authentication, MultipartFile file) {
        //user 조회
        User user = userService.findMyUserInfo(authentication);

        return fileUploadService.uploadImage(file);
    }


    // exhibition 리스트 조회
    public List<ExhibitionResponseDto> getExhibitionList(int page, int size) {

        Page<Exhibition> pageExhibition = exhibitionRepository.findAll(PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt")));

        // 들고온 Entity를 Dto에 넣어서 반환하는 코드
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

        //user 조회
        User user = userService.findCurUser(authentication).orElseThrow(
                () -> new IllegalArgumentException("해당 회원이 존재하지않습니다")
        );

        //Exhibition 존재 여부 확인
        Exhibition exhibition = this.findExhibition(exhibitionId);

        ExhibitionResponseDto exhibitionResponseDto;
        String thumbnail = "";

        ExhibitionDto exhibitionDto = jsonToExhibition(requestBody);

        // 작성자와 로그인한 사용자가 같다면
        if (user.getId().equals(exhibition.getExhibitionUser().getId())) {

            if (file == null) {
                exhibitionDto.setThumbnail(exhibition.getThumbnail());
            } else {
                exhibitionDto.setThumbnail(fileUploadService.uploadImage(file));
            }
            exhibition.update(exhibitionDto);
            exhibition.setUser(user);

            Exhibition exhibitionReponse = exhibitionRepository.save(exhibition);
            exhibitionResponseDto = new ExhibitionResponseDto();

            return exhibitionResponseDto.entityToDto(exhibitionReponse);

            // 작성자와 로그인한 사용자가 다르다면
        } else {
            return null;
        }
    }

    // Exhibition 수정
    @Transactional
    public ExhibitionResponseDto updateExhibitionImg(Authentication authentication, Long exhibitionId, MultipartFile file) {
        ExhibitionResponseDto exhibitionResponseDto;
        //user 조회
        User user = userService.findMyUserInfo(authentication);

        //Exhibition 존재 여부 확인
        Exhibition exhibition = this.findExhibition(exhibitionId);

        // 작성자와 로그인한 같다면
        if (user.getId().equals(exhibition.getExhibitionUser().getId())) {

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
        User user = userService.findMyUserInfo(authentication);

        //Exhibition 존재 여부 확인
        Exhibition exhibition = this.findExhibition(exhibitionId);

        //user와 Exhibition 작성자가 일치한다면
        if (user.getId().equals(exhibition.getExhibitionUser().getId())) {
            user.getExhibitions().remove(exhibition);
            exhibitionRepository.deleteById(exhibitionId);
            return "Success";
        } else {
            return "fail";
        }
    }
}
