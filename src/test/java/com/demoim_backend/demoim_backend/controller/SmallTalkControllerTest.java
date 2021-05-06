package com.demoim_backend.demoim_backend.controller;

import com.demoim_backend.demoim_backend.config.auth.PrincipalDetails;
import com.demoim_backend.demoim_backend.dto.SignupRequestDto;
import com.demoim_backend.demoim_backend.dto.SmallTalkDto;
import com.demoim_backend.demoim_backend.mockCustomUser.WithMockCustomUser;
import com.demoim_backend.demoim_backend.model.SmallTalk;
import com.demoim_backend.demoim_backend.model.User;
import com.demoim_backend.demoim_backend.repository.SmallTalkRepository;
import com.demoim_backend.demoim_backend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.runner.RunWith;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;



@RunWith(SpringRunner.class)
@SpringBootTest
public class SmallTalkControllerTest {

    @Autowired
    SmallTalkRepository smallTalkRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    //매번 테스트가 시작되기 전에 MockMvc 인스턴스를 생성합니다.
    @Before
    public void setup(){
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }


    @After
    public void tearDown() throws Exception{
        smallTalkRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    @Transactional
    public void smallTalk_등록() throws Exception{

        //given
        // 유저생성.
        SignupRequestDto requestDto = new SignupRequestDto("test@naver.com","test123*","test","backend");


        User user = new User(requestDto);
        userRepository.save(user);

        //authentication 생성
        PrincipalDetails userDetails = new PrincipalDetails(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // smalltalk 생성
        String content = "content";

        SmallTalkDto smallTalkDto = SmallTalkDto.builder()
                .contents(content)
                .build();

        //when
        mvc.perform(MockMvcRequestBuilders.post("/api/smalltalk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(smallTalkDto)
                .concat(new ObjectMapper().writeValueAsString(authentication))))
                .andExpect(status().isOk());

        //then
        List<SmallTalk> all = smallTalkRepository.findAll();
        assertThat(all.get(0).getContents()).isEqualTo("content");
        assertThat(all.get(0).getSmallTalkUser().getUsername()).isEqualTo("test@naver.com");
        assertThat(all.get(0).getSmallTalkUser().getPassword()).isEqualTo("test123*");
        assertThat(all.get(0).getSmallTalkUser().getNickname()).isEqualTo("test");
        assertThat(all.get(0).getSmallTalkUser().getPosition()).isEqualTo("backend");

    }

    @Test
    public void exhibition_조회() throws Exception{

        //given

        //authentication 생성
        SignupRequestDto requestDto = new SignupRequestDto("test@naver.com","test123*","test","backend");


        User user = new User(requestDto);
        userRepository.save(user);


        PrincipalDetails userDetails = new PrincipalDetails(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);


        // smalltalk 생성
        String content = "content";

        SmallTalk smallTalk = SmallTalk.builder()
                .contents(content)
                .build();
        smallTalk.setUser(user);

        smallTalkRepository.save(smallTalk);


        //when
        MultiValueMap<String, String> paging = new LinkedMultiValueMap<>();

        paging.add("page", "1");
        paging.add("size", "5");

        mvc.perform(get("/api/smalltalk")
                .params(paging))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        List<SmallTalk> all = smallTalkRepository.findAll();
        assertThat(all.size()).isEqualTo(1);

    }

    @Test
    @Transactional
    public void smalltalk_수정() throws Exception {
        //give

        //authentication 생성
        SignupRequestDto requestDto = new SignupRequestDto("test@naver.com","test123*","test","backend");


        User user = new User(requestDto);
        userRepository.save(user);


        PrincipalDetails userDetails = new PrincipalDetails(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // smalltalk 생성
        String content = "content";


        SmallTalk smallTalk = SmallTalk.builder()
                                        .contents(content)
                                        .build();
        smallTalk.setUser(user);

        smallTalkRepository.save(smallTalk);

        //when
        String id = smallTalk.getId().toString();

        SmallTalkDto smallTalkDtoUpdate = SmallTalkDto.builder()
                .contents("수정")
                .build();

        mvc.perform(MockMvcRequestBuilders.put("/api/smalltalk/detail")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(smallTalkDtoUpdate)
                 .concat(new ObjectMapper().writeValueAsString(authentication)))
                .param("smalltalk_id",id))
                .andExpect(status().isOk());


        //then
        List<SmallTalk> all = smallTalkRepository.findAll();
        assertThat(all.get(0).getContents()).isEqualTo("수정");

        assertThat(all.get(0).getSmallTalkUser().getUsername()).isEqualTo("test@naver.com");
        assertThat(all.get(0).getSmallTalkUser().getPassword()).isEqualTo("test123*");
        assertThat(all.get(0).getSmallTalkUser().getNickname()).isEqualTo("test");
        assertThat(all.get(0).getSmallTalkUser().getPosition()).isEqualTo("backend");

    }

    @Test
    @Transactional
    public void smalltalk_삭제() throws Exception {
        //give

        //authentication 생성
        SignupRequestDto requestDto = new SignupRequestDto("test@naver.com","test123*","test","backend");


        User user = new User(requestDto);
        userRepository.save(user);


        PrincipalDetails userDetails = new PrincipalDetails(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // smalltalk 생성

        SmallTalk smallTalk = SmallTalk.builder()
                .contents("content")
                .build();
        smallTalk.setUser(user);
        smallTalkRepository.save(smallTalk);

        //smalltalk 조회

        List<SmallTalk> all = smallTalkRepository.findAll();
        assertThat(all.size()).isEqualTo(1);


        //when

        String id = all.get(0).getId().toString();

        mvc.perform(MockMvcRequestBuilders.delete("/api/smalltalk/detail")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(authentication))
                .param("smalltalk_id",id))
                .andExpect(status().isOk());


        //then
        List<SmallTalk> deleteResult = smallTalkRepository.findAll();
        assertThat(deleteResult.size()).isEqualTo(0);
    }


}