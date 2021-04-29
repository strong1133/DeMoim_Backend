package com.demoim_backend.demoim_backend.model;

import com.demoim_backend.demoim_backend.dto.SignupRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username; // email_형식

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname; // 사용자 이름

    @Column(nullable = false)
    private String position; //(여러개일경우 List<String>)

    @Column(nullable = true)
    private String desc; // 자기소개

    @Column(nullable = true)
    private String profileimage; //일단 이거는 사항 바꾼거 프론트에 알려주기(0426 17:10)


    @JoinColumn
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Team> teams = new ArrayList<>();
//
//    @JoinColumn
//    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
//    private List<Exhibition> exhibitions = new ArrayList<>();
//
//    @JoinColumn
//    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
//    private List<SmallTalk> smallTalks = new ArrayList<>();



    // 회원가입 생성자
    public User(SignupRequestDto signupRequestDto){
        this.username = signupRequestDto.getUsername();
        this.password = signupRequestDto.getPassword();
        this.nickname = signupRequestDto.getNickname();
        this.position = signupRequestDto.getPosition();
    }


}
