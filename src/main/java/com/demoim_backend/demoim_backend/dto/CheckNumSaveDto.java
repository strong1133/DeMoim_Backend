package com.demoim_backend.demoim_backend.dto;
import com.demoim_backend.demoim_backend.model.CheckNum;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;


@RequiredArgsConstructor
@Getter
public class CheckNumSaveDto {

    @Column(nullable = false)
    private String certNumber;

    @Column(nullable = false)
    private String username;

    @Builder
    public CheckNumSaveDto(String certNumber, String username) {
        this.certNumber = certNumber;
        this.username = username;
    }

    public CheckNum CheckNumDtoToEntity(){
        return CheckNum.builder()
                .username(username)
                .certNumber(certNumber)
                .build();
    }

}

