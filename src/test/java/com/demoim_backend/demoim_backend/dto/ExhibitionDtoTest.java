package com.demoim_backend.demoim_backend.dto;

import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


public class ExhibitionDtoTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @Before
    public void inti() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void valid_테스트() {
        //given
        String title = "";

        String contents = "test";

        String thumbnail = "test.jpg";

        //when
        ExhibitionDto exhibitionDto = new ExhibitionDto();
        exhibitionDto.setTitle(title);
        exhibitionDto.setContents(contents);
        exhibitionDto.setThumbnail(thumbnail);

        Set<ConstraintViolation<ExhibitionDto>> violations = validator.validate(exhibitionDto);
        Iterator<ConstraintViolation<ExhibitionDto>> iterator = violations.iterator();

        List<String> messages = new ArrayList<>();
        while (iterator.hasNext()) {
            ConstraintViolation<ExhibitionDto> next = iterator.next();
            messages.add(next.getMessage());
        }
        assertThat(messages).contains("제목을 입력해주세요.");
    }
}