package com.demoim_backend.demoim_backend.controller;

import com.demoim_backend.demoim_backend.config.auth.PrincipalDetails;
import com.demoim_backend.demoim_backend.dto.ExhibitionDto;
import com.demoim_backend.demoim_backend.dto.SignupRequestDto;
import com.demoim_backend.demoim_backend.mockCustomUser.WithMockCustomUser;
import com.demoim_backend.demoim_backend.mockCustomUser.WithMockCustomUserSecurityContextFactory;
import com.demoim_backend.demoim_backend.model.Exhibition;
import com.demoim_backend.demoim_backend.model.SmallTalk;
import com.demoim_backend.demoim_backend.model.User;
import com.demoim_backend.demoim_backend.repository.ExhibitionRepository;
import com.demoim_backend.demoim_backend.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExhibitionControllerTest {


    @Autowired
    private ExhibitionRepository exhibitionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;


    @BeforeTransaction
    public void setup(){
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @AfterTransaction
    public void tearDown() throws Exception{
        exhibitionRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    @Transactional
    @WithMockCustomUser(username = "test@naver.com",password = "test123456",nickname = "test")
    public  void 프로젝트_조회() throws Exception {
        //given
        // 유저생성.
        SignupRequestDto requestDto = new SignupRequestDto("test@naver.com","test123*","test","backend");
        User user = new User(requestDto);
        userRepository.save(user);

        //authentication 생성
        PrincipalDetails userDetails = new PrincipalDetails(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // exhibitionDto
        ExhibitionDto exhibitionDto = ExhibitionDto.builder()
                .contents("contents")
                .title("title")
                .thumbnail("thumbnail")
                .build();

        Exhibition exhibition = exhibitionDto.toEntity();
        exhibition.setUser(user);
        exhibitionRepository.save(exhibition);

        // when
        MultiValueMap<String, String> paging = new LinkedMultiValueMap<>();

        paging.add("page", "1");
        paging.add("size", "5");

        mvc.perform(get("/api/exhibition")
                .params(paging))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        List<Exhibition> exhibitions = exhibitionRepository.findAll();
        assertThat(exhibitions.size()).isEqualTo(1);

        assertThat(exhibitions.get(0).getTitle()).isEqualTo("title");
        assertThat(exhibitions.get(0).getContents()).isEqualTo("contents");
        assertThat(exhibitions.get(0).getThumbnail()).isEqualTo("thumbnail");

        assertThat(exhibitions.get(0).getExhibitionUser().getUsername()).isEqualTo("test@naver.com");
        assertThat(exhibitions.get(0).getExhibitionUser().getPassword()).isEqualTo("test123*");
        assertThat(exhibitions.get(0).getExhibitionUser().getNickname()).isEqualTo("test");
        assertThat(exhibitions.get(0).getExhibitionUser().getPosition()).isEqualTo("backend");
    }


    @Test
    @Transactional
    @WithMockCustomUser(username = "test@naver.com",password = "test123456",nickname = "test")
    public void 프로젝트자랑하기_삭제() throws Exception {
        //given
        // 유저생성.
        SignupRequestDto requestDto = new SignupRequestDto("test@naver.com","test123*","test","backend");
        User user = new User(requestDto);
        userRepository.save(user);

        //authentication 생성
        PrincipalDetails userDetails = new PrincipalDetails(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // exhibitionDto
        ExhibitionDto exhibitionDto = ExhibitionDto.builder()
                .contents("contents")
                .title("title")
                .thumbnail("thumbnail")
                .build();
        Exhibition exhibition = exhibitionDto.toEntity();
        exhibition.setUser(user);
        exhibitionRepository.save(exhibition);

        //exhibition 조회
        List<Exhibition> all = exhibitionRepository.findAll();
        assertThat(all.size()).isEqualTo(1);

        //when
        String id = all.get(0).getId().toString();

        mvc.perform(MockMvcRequestBuilders.delete("/api/exhibition/detail")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(authentication))
                .param("exhibition_id",id))
                .andExpect(status().isOk());

        //exhibition 조회
        List<Exhibition> exhibitions = exhibitionRepository.findAll();
        assertThat(exhibitions.size()).isEqualTo(0);
    }

}