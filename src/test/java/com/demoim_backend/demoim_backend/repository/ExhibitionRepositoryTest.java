package com.demoim_backend.demoim_backend.repository;

import com.demoim_backend.demoim_backend.dto.ExhibitionDto;
import com.demoim_backend.demoim_backend.dto.SignupRequestDto;
import com.demoim_backend.demoim_backend.model.Exhibition;
import com.demoim_backend.demoim_backend.model.SmallTalk;
import com.demoim_backend.demoim_backend.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExhibitionRepositoryTest {

    @Autowired
    ExhibitionRepository exhibitionRepository;

    @Autowired
    UserRepository userRepository;

    String title = "test";
    String contents = "contents";
    String thumbnail = "test.jpg";

    @BeforeTransaction
    public void setup(){

        SignupRequestDto requestDto = new SignupRequestDto();
        requestDto.setUsername("test@naver.com");
        requestDto.setPassword("test123*");
        requestDto.setNickname("test");
        requestDto.setPosition("backend");

        User user = new User(requestDto);
        userRepository.save(user);
        LocalDateTime now = LocalDateTime.of(2021, 4, 17, 0, 0, 0);

        Exhibition exhibition = Exhibition.builder()
                .title(title)
                .contents(contents)
                .thumbnail(thumbnail)
                .build();
        exhibition.setUser(user);
        exhibitionRepository.save(exhibition);
    }

    @AfterTransaction
    public void cleanup(){
        userRepository.deleteAll();
        exhibitionRepository.deleteAll();
    }

    @Test
    @Transactional
    public void 프로젝트자랑하기_등록(){

        //when
        List<Exhibition> exhibitions = exhibitionRepository.findAll();
        Exhibition exhibition = exhibitions.get(0);

        //then
        assertThat(exhibition.getTitle()).isEqualTo("test");
        assertThat(exhibition.getContents()).isEqualTo("contents");
        assertThat(exhibition.getThumbnail()).isEqualTo("test.jpg");
        assertThat(exhibition.getExhibitionUser().getUsername()).isEqualTo("test@naver.com");
        assertThat(exhibition.getExhibitionUser().getPassword()).isEqualTo("test123*");
        assertThat(exhibition.getExhibitionUser().getNickname()).isEqualTo("test");
        assertThat(exhibition.getExhibitionUser().getPosition()).isEqualTo("backend");

    }

    @Test
    @Transactional
    public void 프로젝트자랑하기_수정(){

        //given
        List<Exhibition> exhibitions = exhibitionRepository.findAll();
        Exhibition exhibition = exhibitions.get(0);

        //when

        title = "수정";
        contents ="수정 컨텐트";
        thumbnail = "수정 썸네일";

        ExhibitionDto exhibitionDto = ExhibitionDto.builder()
                .title(title)
                .contents(contents)
                .thumbnail(thumbnail)
                .build();

       exhibition.update(exhibitionDto);

        //then
        assertThat(exhibition.getTitle()).isEqualTo(title);
        assertThat(exhibition.getContents()).isEqualTo(contents);
        assertThat(exhibition.getThumbnail()).isEqualTo(thumbnail);
        assertThat(exhibition.getExhibitionUser().getUsername()).isEqualTo("test@naver.com");

    }

    @Test
    @Transactional
    public void 프로젝트_페이징_조회하기(){

        //given
        int page =1;
        int size = 10;

        Page<Exhibition> pageSmallTalk = exhibitionRepository.findAll(PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt")));
        List<Exhibition> exhibitionsList = pageSmallTalk.getContent();

        //then
        assertThat(exhibitionsList.get(0).getExhibitionUser().getUsername()).isEqualTo("test@naver.com");
        assertThat(exhibitionsList.size()).isEqualTo(1);
    }

    @Test
    @Transactional
    public void 프로젝트자랑하기_user조회(){

        //given
        List<Exhibition> exhibitionsList = exhibitionRepository.findAll();
        Exhibition exhibition = exhibitionsList.get(0);

        //then
        assertThat(exhibition.getExhibitionUser().getUsername()).isEqualTo("test@naver.com");
        assertThat(exhibition.getExhibitionUser().getPassword()).isEqualTo("test123*");
        assertThat(exhibition.getExhibitionUser().getNickname()).isEqualTo("test");
        assertThat(exhibition.getExhibitionUser().getPosition()).isEqualTo("backend");

    }

    @Test
    @Transactional
    public void smalltalk_삭제(){
        //given
        List<Exhibition> exhibitions = exhibitionRepository.findAll();
        Long exhibition_id = exhibitions.get(0).getId();

        //when
        exhibitionRepository.deleteAll();

        //then
        List<Exhibition> exhibitionList = exhibitionRepository.findAll();

        assertThat(exhibitionList.size()).isEqualTo(0);

    }

}