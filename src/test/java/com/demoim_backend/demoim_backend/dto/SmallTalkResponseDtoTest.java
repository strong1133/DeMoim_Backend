package com.demoim_backend.demoim_backend.dto;

import org.junit.Before;
import org.junit.Test;

import javax.validation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class SmallTalkResponseDtoTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @Before
    public void init() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void 빌드테스트() {
        //given
        String contents = "contents";

        //when
        SmallTalkResponseDto responseDto = SmallTalkResponseDto.builder()
                .contents(contents)
                .build();

        assertThat(responseDto.getContents()).isEqualTo(contents);

    }

    @Test
    public void valid_테스트() {
        String contents = "";

        SmallTalkDto smallTalkDto = SmallTalkDto.builder()
                .contents(contents)
                .build();


        Set<ConstraintViolation<SmallTalkDto>> violations = validator.validate(smallTalkDto);
        Iterator<ConstraintViolation<SmallTalkDto>> iterator = violations.iterator();

        List<String> messages = new ArrayList<>();
        while (iterator.hasNext()) {
            ConstraintViolation<SmallTalkDto> next = iterator.next();
            messages.add(next.getMessage());
        }
        assertThat(messages).contains("내용을 입력하세요.");
    }
}