package com.demoim_backend.demoim_backend.controller;

import com.demoim_backend.demoim_backend.dto.*;
import com.demoim_backend.demoim_backend.mockCustomUser.WithMockCustomUser;
import com.demoim_backend.demoim_backend.model.Exhibition;
import com.demoim_backend.demoim_backend.model.User;
import com.demoim_backend.demoim_backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;

import com.demoim_backend.demoim_backend.repository.ExhibitionRepository;
import com.demoim_backend.demoim_backend.repository.UserRepository;
import com.demoim_backend.demoim_backend.service.ExhibitionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;




@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ExhibitionControllerTest {


    @Autowired
    UserRepository userRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @MockBean
    private ExhibitionService exhibitionService;

    @MockBean
    private UserService userService;

    @MockBean
    private ExhibitionRepository exhibitionRepository;

    @Before
    public void setup(){
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }


    @Test
    public void 프로젝트자랑하기_등록() throws Exception {

        //given
        // 형식을 맞추주기위한 빈 객체 생성
        User user = new User();

        ExhibitionDto exhibitionDto = new ExhibitionDto();

        ResponseUserDto responseUserDto = new ResponseUserDto();

        ExhibitionResponseDto exhibitionResponseDto = new ExhibitionResponseDto();

        MockMultipartFile file = new MockMultipartFile("file", "img.jpg", "/jpg,png", "data".getBytes(StandardCharsets.UTF_8));

        Authentication authentication = mock(Authentication.class);

        //when

        given(exhibitionService.createExhibition(authentication,exhibitionDto.toString(),file))
                .willReturn(exhibitionResponseDto);

        given(userService.findCurUser(authentication)).willReturn(java.util.Optional.of(user));

        mvc.perform(multipart("/api/exhibition")
                .file("requestBody",exhibitionDto.toString().getBytes(StandardCharsets.UTF_8))
                .file(file)
                .param("authentication",new ObjectMapper().writeValueAsString(authentication)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public  void 프로젝트_조회() throws Exception {

        // given
        // 형식을 위한 빈 객체 생성
        ResponseUserDto responseUserDto = new ResponseUserDto();
        ExhibitionResponseDto exhibitionResponseDto = new ExhibitionResponseDto();
        List<ExhibitionResponseDto> exhibitionsGiven = new ArrayList<>();
        exhibitionsGiven.add(exhibitionResponseDto);

        // when
        given(exhibitionService.getExhibitionList(1,5)).willReturn(exhibitionsGiven);

        MultiValueMap<String, String> paging = new LinkedMultiValueMap<>();
        paging.add("page", "1");
        paging.add("size", "5");

        mvc.perform(get("/api/exhibition")
                .params(paging))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    @WithMockCustomUser
    public void 프로젝트자랑하기_수정() throws Exception {

        //given
        // 형식을 맞추주기위한 빈 객체 생성
        User user = new User();

        ResponseUserDto responseUserDto = new ResponseUserDto();

        ExhibitionDto exhibitionDto = new ExhibitionDto();

        Exhibition exhibition = new Exhibition();

        ExhibitionResponseDto exhibitionResponseDto = new ExhibitionResponseDto();

        MockMultipartFile file = new MockMultipartFile("file", "img.jpg", "/jpg,png", "data".getBytes(StandardCharsets.UTF_8));

        Authentication authentication = mock(Authentication.class);

        Long id = 1L;

        //when
        given(userService.findCurUser(authentication))
                .willReturn(Optional.of(user));

        given(exhibitionService.updateExhibition(authentication, exhibitionDto.toString(),file,id))
                .willReturn(exhibitionResponseDto);

        given(exhibitionRepository.findById(id))
                .willReturn(Optional.of(exhibition));


        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/api/exhibition/detail");
            builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });
        mvc.perform(builder
                .file(file)
                .file("requestBody",exhibitionDto.toString().getBytes(StandardCharsets.UTF_8))
                .param("exhibition_id",id.toString())
                .param("authentication",new ObjectMapper().writeValueAsString(authentication)));

    }


    @Test
    public void 프로젝트자랑하기_삭제() throws Exception {

        User user = new User();
        Exhibition exhibition = new Exhibition();

        Authentication authentication = mock(Authentication.class);
        Long id = 1L;

        given(userService.findCurUser(authentication))
                .willReturn(Optional.of(user));
        given(exhibitionService.deleteExhibition(authentication, id)).willReturn("success");
        given(exhibitionRepository.findById(id)).willReturn(Optional.of(exhibition));

        mvc.perform(delete("/api/exhibition/detail")
                .content(new ObjectMapper().writeValueAsString(authentication))
                .param("exhibition_id", id.toString()))
                .andExpect(status().is4xxClientError());

    }
}