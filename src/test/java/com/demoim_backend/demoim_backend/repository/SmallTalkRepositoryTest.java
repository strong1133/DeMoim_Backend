package com.demoim_backend.demoim_backend.repository;

import com.demoim_backend.demoim_backend.dto.SignupRequestDto;
import com.demoim_backend.demoim_backend.dto.SmallTalkDto;
import com.demoim_backend.demoim_backend.dto.SmallTalkResponseDto;
import com.demoim_backend.demoim_backend.model.SmallTalk;
import com.demoim_backend.demoim_backend.model.User;
import org.junit.After;
import org.junit.Before;
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
public class SmallTalkRepositoryTest {

    @Autowired
    SmallTalkRepository smallTalkRepository;

    @Autowired
    UserRepository userRepository;


    String contents = "contents";

    @BeforeTransaction
    public void setup() {

        SignupRequestDto requestDto = new SignupRequestDto();
        requestDto.setUsername("test@naver.com");
        requestDto.setPassword("test123*");
        requestDto.setNickname("test");
        requestDto.setPosition("backend");

        User user = new User(requestDto);
        userRepository.save(user);
        LocalDateTime now = LocalDateTime.of(2021, 4, 17, 0, 0, 0);

        SmallTalk smallTalk = SmallTalk.builder()
                .contents(contents)
                .build();
        smallTalk.setUser(user);
        smallTalkRepository.save(smallTalk);
    }

    @AfterTransaction
    public void cleanup() {
        userRepository.deleteAll();
        smallTalkRepository.deleteAll();
    }

    @Test
    @Transactional
    public void smallTalk_등록() {

        List<SmallTalk> smallTalks = smallTalkRepository.findAll();
        SmallTalk smallTalk = smallTalks.get(0);

        //when
        assertThat(smallTalk.getContents()).isEqualTo("contents");
        assertThat(smallTalk.getSmallTalkUser().getUsername()).isEqualTo("test@naver.com");
        assertThat(smallTalk.getSmallTalkUser().getPassword()).isEqualTo("test123*");
        assertThat(smallTalk.getSmallTalkUser().getNickname()).isEqualTo("test");
        assertThat(smallTalk.getSmallTalkUser().getPosition()).isEqualTo("backend");
    }

    @Test
    @Transactional
    public void smalltalk_수정하기() {

        //given
        List<SmallTalk> smallTalks = smallTalkRepository.findAll();
        SmallTalk smallTalkResult = smallTalks.get(0);

        //when

        contents = "test2";

        SmallTalkDto smallTalkUpdate = new SmallTalkDto();
        smallTalkUpdate.setContents(contents);
        smallTalkResult.Update(smallTalkUpdate);

        //then
        assertThat(smallTalkResult.getContents()).isEqualTo("test2");
        assertThat(smallTalkResult.getSmallTalkUser().getUsername()).isEqualTo("test@naver.com");

    }

    @Test
    @Transactional
    public void smalltalk_페이징_조회하기() {

        //given
        int page = 1;
        int size = 10;

        //when
        Page<SmallTalk> pageSmallTalk = smallTalkRepository.findAll(PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt")));
        List<SmallTalk> smallTalkEntitys = pageSmallTalk.getContent();

        //then
        assertThat(smallTalkEntitys.get(0).getSmallTalkUser().getUsername()).isEqualTo("test@naver.com");

    }

    @Test
    @Transactional
    public void smalltalk_user조회() {
        //given
        List<SmallTalk> smallTalks = smallTalkRepository.findAll();
        SmallTalk smallTalkResult = smallTalks.get(0);

        //when
        assertThat(smallTalkResult.getSmallTalkUser().getUsername()).isEqualTo("test@naver.com");
        assertThat(smallTalkResult.getSmallTalkUser().getPassword()).isEqualTo("test123*");
        assertThat(smallTalkResult.getSmallTalkUser().getNickname()).isEqualTo("test");
        assertThat(smallTalkResult.getSmallTalkUser().getPosition()).isEqualTo("backend");
    }

    @Test
    @Transactional
    public void smalltalk_삭제() {
        //given
        List<SmallTalk> smallTalks = smallTalkRepository.findAll();
        Long smallTalkId = smallTalks.get(0).getId();

        //when
        smallTalkRepository.deleteAll();

        //then
        List<SmallTalk> smallTalkResult = smallTalkRepository.findAll();

        assertThat(smallTalkResult.size()).isEqualTo(0);
    }
}